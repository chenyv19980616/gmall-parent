package com.atgugiu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.search.SearchFeignClient;
import com.atguigu.gmall.model.vo.search.SearchParamVo;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenyv
 * @create 2022-09-15 15:59
 */
@Controller
public class SearchController {

    @Autowired
    SearchFeignClient searchFeignClient;

    /**
     * 检索列表页
     * 检索条件参数：
     * 1. 按照分类：category3Id  category2Id  category1Id
     * 2. 按照关键字：keyword
     * 3. 按照属性：props
     * 4. 按照品牌
     * 5. 分页
     * 6. 排序
     * <p>
     * vo:view object:专门封装页面数据，给页面交数据
     *
     * @return
     */
    @GetMapping("/list.html")
    public String search(SearchParamVo searchParamVo, Model model, HttpServletRequest request) {

        Result<SearchResponseVo> search = searchFeignClient.search(searchParamVo);
        SearchResponseVo data = search.getData();

        //把result数据展示到页面
        //1. 以前检索页面传来的所有条件，原封不动返回给页面
        model.addAttribute("searchParam", data.getSearchParam());
        //2. 品牌面包屑位置的显示
        model.addAttribute("trademarkParam", data.getTrademarkParam());
        //3. 属性面包屑，是集合，集合里面每一个元素是一个对象，拥有数据（attrName、attrValue、attrId）
        model.addAttribute("propsParamList", data.getPropsParamList());
        //4. 所有品牌，是集合，集合里面每个元素是一个对象，拥有数据（tmId、tmLogo、tmName）
        model.addAttribute("trademarkList", data.getTrademarkList());
        //5. 所有属性，是集合，集合里面每个元素是一个对象，拥有数据（attrId,attrName、List<String> attrValueList）
        model.addAttribute("attrList", data.getAttrList());
        //6. 排序信息,是对象，拥有数据（type、sort）
        model.addAttribute("orderMap", data.getOrderMap());
        //7. 所有商品列表,是集合，集合里面每个元素是一个对象，拥有这些数据（es中每个商品的详细数据）
        model.addAttribute("goodsList", data.getGoodsList());
        //8. 分页信息，
        model.addAttribute("pageNo", data.getPageNo());
        model.addAttribute("totalPages", data.getTotalPages());
        //9. url信息
        model.addAttribute("urlParam", data.getUrlParam());
        return "list/index";
    }
}
