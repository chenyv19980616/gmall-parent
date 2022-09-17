package com.atguigu.gmall.model.vo.search;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchAttr;
import lombok.Data;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-09-16 11:21
 * 给检索页响应的所有数据
 */
@Data
public class SearchResponseVo {

    //检索用的所有参数
    private SearchParamVo searchParam;
    //品牌的面包屑位置
    private String trademarkParam;
    //平台属性面包屑
    private List<SearchAttr> propsParamList;

    //所有品牌列表
    private List<TrademarkVo> trademarkList;
    //所有属性列表
    private List<AttrVo> attrList;

    //排序信息
    private OrderMapVo orderMap;
    //检索到的商品集合
    private List<Goods> goodsList;

    //当前页
    private Integer pageNo;
    //总页
    private Integer totalPages;

    //url整个参数链接
    private String urlParam;
}
