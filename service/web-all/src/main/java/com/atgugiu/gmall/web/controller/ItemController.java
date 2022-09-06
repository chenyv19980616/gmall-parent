package com.atgugiu.gmall.web.controller;

import com.atgugiu.gmall.web.feign.SkuDetailFeignClient;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author chenyv
 * @create 2022-08-31 14:55
 * 商品详情
 */
@Controller
public class ItemController {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    /**
     * 商品详情页展示
     * @param skuId
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String item(@PathVariable("skuId") Long skuId, Model model) {

        Result<SkuDetailTo> result = skuDetailFeignClient.getSkuDetail(skuId);
        if (result.isOk()) {
            SkuDetailTo skuDetailTo = result.getData();
            if (skuDetailTo.getSkuInfo()==null) {
                //说明远程没有查到商品
                return "item/404";
            }
            model.addAttribute("categoryView", skuDetailTo.getCategoryView());
            model.addAttribute("skuInfo", skuDetailTo.getSkuInfo());
            model.addAttribute("price", skuDetailTo.getPrice());
            model.addAttribute("spuSaleAttrList", skuDetailTo.getSpuSaleAttrList());
            model.addAttribute("valuesSkuJson", skuDetailTo.getValuesSkuJson());
        }
        return "item/index";
    }
}
