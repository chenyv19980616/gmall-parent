package com.atguigu.gmall.search.service.impl;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.vo.search.*;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.search.service.GoodsService;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author chenyv
 * @create 2022-09-15 15:14
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    ElasticsearchRestTemplate esRestTemplate;

    @Override
    public void saveGoods(Goods goods) {
        goodsRepository.save(goods);
    }

    @Override
    public void deleteGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }

    @Override
    public SearchResponseVo search(SearchParamVo paramVo) {
        //1. 动态构建出搜索条件
        Query query = buildQueryDsl(paramVo);

        //2. 搜索
        SearchHits<Goods> goods =
                esRestTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));

        //3. 将搜索条件进行转化
        SearchResponseVo responseVo = buildSearchResonseResult(goods, paramVo);
        return responseVo;
    }

    @Override
    public void updateHotScore(Long skuId, Long score) {
        //1.  找到商品
        Goods goods = goodsRepository.findById(skuId).get();

        //2. 更新得分
        goods.setHotScore(score);

        //3. 同步到es
        goodsRepository.save(goods);
        
        //ES可以发布修改DSL，只更新hotscore
//        esRestTemplate.update()
    }

    /**
     * 根据检索到的记录，构建响应结果
     * @param goods
     * @return
     */
    private SearchResponseVo buildSearchResonseResult(SearchHits<Goods> goods,
                                                      SearchParamVo paramVo) {
        SearchResponseVo vo = new SearchResponseVo();

        //1. 当时检索前端传来的所有参数
        vo.setSearchParam(paramVo);

        //2. 构建品牌面包屑
        if (!StringUtils.isEmpty(paramVo.getTrademark())) {
            vo.setTrademarkParam("品牌：" + paramVo.getTrademark().split(":")[1]);
        }

        //3. 构建平台属性面包屑
        if (paramVo.getProps() != null && paramVo.getProps().length > 0) {
            List<SearchAttr> propsParamList = new ArrayList<>();
            for (String prop : paramVo.getProps()) {
                //23:8G:运行内存
                String[] split = prop.split(":");
                //一个SearchAttr 代表一个属性面包屑
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(Long.parseLong(split[0]));
                searchAttr.setAttrValue(split[1]);
                searchAttr.setAttrName(split[2]);
                propsParamList.add(searchAttr);
            }
            vo.setPropsParamList(propsParamList);
        }

        //4、所有品牌列表 。需要ES聚合分析
        List<TrademarkVo> trademarkVos = buildTrademarkList(goods);
        vo.setTrademarkList(trademarkVos);

        //5、所有属性列表 。需要ES聚合分析
        List<AttrVo> attrVos = buildAttrList(goods);
        vo.setAttrList(attrVos);

        //为了回显
        //6、返回排序信息  order=1:desc
        if (!StringUtils.isEmpty(paramVo.getOrder())) {
            String order = paramVo.getOrder();
            OrderMapVo mapVo = new OrderMapVo();
            mapVo.setType(order.split(":")[0]);
            mapVo.setSort(order.split(":")[1]);
            vo.setOrderMap(mapVo);
        }

        //7、所有搜索到的商品列表
        List<Goods> goodsList = new ArrayList<>();
        List<SearchHit<Goods>> hits = goods.getSearchHits();
        for (SearchHit<Goods> hit : hits) {
            //这条命中记录的商品
            Goods content = hit.getContent();
            //如果模糊检索了，会有高亮标题
            if (!StringUtils.isEmpty(paramVo.getKeyword())) {
                String highlightTitle = hit.getHighlightField("title").get(0);
                //设置高亮标题
                content.setTitle(highlightTitle);
            }
            goodsList.add(content);
        }

        vo.setGoodsList(goodsList);


        //8、页码
        vo.setPageNo(paramVo.getPageNo());
        //9、总页码？
        long totalHits = goods.getTotalHits();
        long ps = totalHits % SysRedisConst.SEARCH_PAGE_SIZE == 0 ?
                totalHits / SysRedisConst.SEARCH_PAGE_SIZE :
                (totalHits / SysRedisConst.SEARCH_PAGE_SIZE + 1);
        vo.setTotalPages(new Integer(ps + ""));

        //10、老连接。。。   /list.html?category2Id=13
        String url = makeUrlParam(paramVo);
        vo.setUrlParam(url);

        return vo;
    }

    /**
     * 分析当前得到的检索结果中，所有商品涉及了多少种平台属性
     * @param goods
     * @return
     */
    private List<AttrVo> buildAttrList(SearchHits<Goods> goods) {

        List<AttrVo> attrVos = new ArrayList<>();

        //拿到整个属性的聚合结果
        ParsedNested attrAgg = goods.getAggregations().get("attrAgg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            AttrVo attrVo = new AttrVo();

            //提取到属性Id
            Long attrId = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);

            //属性名
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);

            //属性值
            List<String> attrValues = new ArrayList<>();
            ParsedStringTerms attrValue = bucket.getAggregations().get("attrValue");
            for (Terms.Bucket valueBucket : attrValue.getBuckets()) {
                String value = valueBucket.getKeyAsString();
                attrValues.add(value);
            }
            attrVo.setAttrValueList(attrValues);

            attrVos.add(attrVo);
        }
        return attrVos;
    }

    /**
     * 分析当前得到的检索结果中，所有商品涉及了多少中品牌
     *
     * @param goods
     * @return
     */
    private List<TrademarkVo> buildTrademarkList(SearchHits<Goods> goods) {

        List<TrademarkVo> trademarkVos = new ArrayList<>();

        //拿到 tmIdAgg 聚合
        ParsedLongTerms tmIdAgg = goods.getAggregations().get("tmIdAgg");
        //拿到品牌id桶聚合中的每个数据
        for (Terms.Bucket bucket : tmIdAgg.getBuckets()) {
            TrademarkVo trademarkVo = new TrademarkVo();

            //获取品牌id
            Long tmId = bucket.getKeyAsNumber().longValue();
            trademarkVo.setTmId(tmId);

            //获取品牌名
            ParsedStringTerms tmNameAgg = bucket.getAggregations().get("tmNameAgg");
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            trademarkVo.setTmName(tmName);

            //获取品牌Logo
            ParsedStringTerms tmLogoAgg = bucket.getAggregations().get("tmLogoAgg");
            String tmLogoUrl = tmLogoAgg.getBuckets().get(0).getKeyAsString();
            trademarkVo.setTmLogoUrl(tmLogoUrl);

            trademarkVos.add(trademarkVo);
        }
        return trademarkVos;
    }


    /**
     * 制造老链接
     *
     * @param paramVo
     * @return
     */
    private String makeUrlParam(SearchParamVo paramVo) {
        // list.html?&k=v
        StringBuilder builder = new StringBuilder("list.html?");
        //1、拼三级分类所有参数
        if (paramVo.getCategory1Id() != null) {
            builder.append("&category1Id=" + paramVo.getCategory1Id());
        }
        if (paramVo.getCategory2Id() != null) {
            builder.append("&category2Id=" + paramVo.getCategory2Id());
        }
        if (paramVo.getCategory3Id() != null) {
            builder.append("&category3Id=" + paramVo.getCategory3Id());
        }

        //2、拼关键字
        if (!StringUtils.isEmpty(paramVo.getKeyword())) {
            builder.append("&keyword=" + paramVo.getKeyword());
        }

        //3、拼品牌
        if (!StringUtils.isEmpty(paramVo.getTrademark())) {
            builder.append("&trademark=" + paramVo.getTrademark());
        }

        //4、拼属性
        if (paramVo.getProps() != null && paramVo.getProps().length > 0) {
            for (String prop : paramVo.getProps()) {
                //props=23:8G:运行内存
                builder.append("&props=" + prop);
            }
        }
//        //5、拼排序
//        builder.append("&order="+paramVo.getOrder());
//
//        //6、拼页码
//        builder.append("&pageNo="+paramVo.getPageNo());
        //拿到最终字符串
        String url = builder.toString();
        return url;
    }

    /**
     * 根据前端传过来的所有请求参数构建检索条件
     * DSL:
     * 1.查询条件：
     *
     * @param paramVo
     * @return
     */
    private Query buildQueryDsl(SearchParamVo paramVo) {

        //准备bool
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //给bool中准备must的各个条件

        //1. 前端传了分类
        if (paramVo.getCategory1Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category1Id", paramVo.getCategory1Id()));
        }
        if (paramVo.getCategory2Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category2Id", paramVo.getCategory2Id()));
        }
        if (paramVo.getCategory3Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category3Id", paramVo.getCategory3Id()));
        }

        //2. 前端传了keyword
        if (!StringUtils.isEmpty(paramVo.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("title", paramVo.getKeyword()));
        }

        //3. 前端传了品牌trademark
        if (!StringUtils.isEmpty(paramVo.getTrademark())) {
            long tmId = Long.parseLong(paramVo.getTrademark().split(":")[0]);
            boolQuery.must(QueryBuilders.termQuery("tmId", tmId));
        }

        //4. 前端传了属性props
        String[] props = paramVo.getProps();
        if (props != null && props.length > 0) {
            for (String prop : props) {
                //得到属性的值和id
                String[] split = prop.split(":");
                Long attrId = Long.parseLong(split[0]);
                String attrValue = split[1];

                //构造boolQuery
                BoolQueryBuilder nestBool = QueryBuilders.boolQuery();
                nestBool.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestBool.must(QueryBuilders.termQuery("attrs.attrValue", attrValue));

                NestedQueryBuilder nestedQuery =
                        QueryBuilders.nestedQuery("attrs", nestBool, ScoreMode.None);

                boolQuery.must(nestedQuery);
            }
        }
        //==============================检索条件结束===========================

        //准备一个原生搜索条件
        NativeSearchQuery query = new NativeSearchQuery(boolQuery);

        //5. 前端传了排序
        if (!StringUtils.isEmpty(paramVo.getOrder())) {
            String[] split = paramVo.getOrder().split(":");
            //分析排序用哪个字段
            String orderField = "";
            switch (split[0]) {
                case "1":
                    orderField = "hotScore";
                    break;
                case "2":
                    orderField = "price";
                    break;
                case "3":
                    orderField = "createTime";
                    break;
                default:
                    orderField = "hotScore";
            }
            Sort sort = Sort.by(orderField);
            //判断排序的规则
            if (split[1].equals("asc")) {
                sort = sort.ascending();
            } else {
                sort = sort.descending();
            }
            query.addSort(sort);
        }

        //前端传了页码
        //页码在spring底层是从0开始计算的，自己需要-1
        PageRequest pageRequest = PageRequest.of(paramVo.getPageNo() - 1, SysRedisConst.SEARCH_PAGE_SIZE);
        query.setPageable(pageRequest);
        //===============================排序分页结束===============================

        //高亮
        if (!StringUtils.isEmpty(paramVo.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title")
                    .preTags("<span style='color:red'>")
                    .postTags("</span>");
            HighlightQuery highlightQuery = new HighlightQuery(highlightBuilder);
            query.setHighlightQuery(highlightQuery);
        }
        //=============================模糊查询的高亮查询结束=============================

        //==============聚合分析上面DSL检索到的所有商品涉及了多少种品牌和多少种平台属性==============

        //--------品牌聚合
        //品牌Id聚合
        TermsAggregationBuilder tmIdAgg = AggregationBuilders
                .terms("tmIdAgg")
                .field("tmId")
                .size(100);
        //品牌名-子聚合
        TermsAggregationBuilder tmNameAgg = AggregationBuilders
                .terms("tmNameAgg")
                .field("tmName")
                .size(100);
        //品牌Logo-子聚合
        TermsAggregationBuilder tmLogoAgg = AggregationBuilders
                .terms("tmLogoAgg")
                .field("tmLogoUrl")
                .size(1);

        tmIdAgg.subAggregation(tmNameAgg);
        tmIdAgg.subAggregation(tmLogoAgg);

        //品牌id聚合条件拼装完成
        query.addAggregation(tmIdAgg);

        //--------属性的整个嵌入式聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        //--------attrId聚合
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(100);
        //--------attrName聚合
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(100);
        //--------attrValue聚合
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValue").field("attrs.attrValue").size(100);

        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);

        attrAgg.subAggregation(attrIdAgg);

        //添加整个属性的聚合
        query.addAggregation(attrAgg);

        return query;
    }
}
