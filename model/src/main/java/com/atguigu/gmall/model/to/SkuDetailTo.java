package com.atguigu.gmall.model.to;

import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenyv
 * @create 2022-08-31 16:18
 */
@Data
public class SkuDetailTo {
    //当前sku所属的分类信息
    private CategoryViewTo categoryView;

    //商品的基本信息
    private SkuInfo skuInfo;

    //spu的销售属性列表
    private List<SpuSaleAttr> spuSaleAttrList;

    //valueSkuJson
    private String valuesSkuJson;

    //实时价格
    private BigDecimal price;
}
