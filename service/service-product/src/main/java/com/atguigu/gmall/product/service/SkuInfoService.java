package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.list.Goods;
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

    /**
     * sku信息大保存
     *
     * @param info
     */
    void saveSkuInfo(SkuInfo info);

    /**
     * 商品下架
     *
     * @param skuId
     */
    void cancelSale(Long skuId);

    /**
     * 商品上架
     *
     * @param skuId
     */
    void onSale(Long skuId);

    /**
     * 获取sku商品详情数据
     *
     * @param skuId
     * @return
     */
    SkuDetailTo getSkuDetail(Long skuId);

    /**
     * 获取sku的实时价格
     *
     * @param skuId
     * @return
     */
    BigDecimal get1010price(Long skuId);

    /**
     * 查询sku的基本信息
     *
     * @param skuId
     * @return
     */
    SkuInfo getDetailSkuInfo(Long skuId);

    /**
     * 查询sku的图片信息
     *
     * @param skuId
     * @return
     */
    List<SkuImage> getDetailSkuImages(Long skuId);

    /**
     * 找到所有的商品id
     *
     * @return
     */
    List<Long> findAllSkuId();

    /**
     * 得到某个sku在es中需要存储的所有属性
     *
     * @param skuId
     * @return
     */
    Goods getGoodsBySkuId(Long skuId);
}
