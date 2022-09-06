package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenyv
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service
 * @createDate 2022-08-26 16:46:50
 */
public interface SkuInfoService extends IService<SkuInfo> {

    //sku信息大保存
    void saveSkuInfo(SkuInfo info);

    //商品下架
    void cancelSale(Long skuId);

    //商品上架
    void onSale(Long skuId);

    //获取sku商品详情数据
    SkuDetailTo getSkuDetail(Long skuId);

    //获取sku的实时价格
    BigDecimal get1010price(Long skuId);

    //查询sku的基本信息
    SkuInfo getDetailSkuInfo(Long skuId);

    //查询sku的图片信息
    List<SkuImage> getDetailSkuImages(Long skuId);

    //找到所有的商品id
    List<Long> findAllSkuId();

}
