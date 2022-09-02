package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenyv
 * @create 2022-08-31 16:50
 */
@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    @Resource
    ThreadPoolExecutor executor;

//    本地缓存容器
//    private Map<Long, SkuDetailTo> skuCache = new ConcurrentHashMap<>();

    /**
     * Redis缓存容器
     */
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        // 1.查缓存  sku:info:52
        String jsonStr = redisTemplate.opsForValue().get("sku:info:" + skuId);
        if ("x".equals(jsonStr)) {
            return null;
        }
        if (StringUtils.isEmpty(jsonStr)) {
            //Redis没有缓存数据(回源)
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            //查到的对象转为Json字符串放到缓存
            String cacheJson = "x";
            if (fromRpc != null) {
                cacheJson = Jsons.toStr(fromRpc);
                redisTemplate.opsForValue().set("sku:info:" + skuId, cacheJson, 7, TimeUnit.DAYS);
            } else {
                redisTemplate.opsForValue().set("sku:info:" + skuId, cacheJson, 30, TimeUnit.MINUTES);
            }
            return fromRpc;
        }
        //Redis有缓存数据,把Json转成指定的对象
        SkuDetailTo skuDetailTo = Jsons.toObj(jsonStr, SkuDetailTo.class);
        return skuDetailTo;
    }

//    缓存优化后使用本地缓存
//    @Override
//    public SkuDetailTo getSkuDetail(Long skuId) {
//
//        // 1.先看缓存
//        SkuDetailTo cacheData = skuCache.get(skuId);
//        // 2.判断
//        if (cacheData == null) {
//            // 3.缓存没有,真正查询【回源（回到数据源头真正检索）】【提高缓存命中率】
//            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
//            skuCache.put(skuId, fromRpc);
//            return fromRpc;
//        }
//        return cacheData;

    //    }
    //未缓存优化前
    public SkuDetailTo getSkuDetailFromRpc(Long skuId) {
        SkuDetailTo detailTo = new SkuDetailTo();

        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            // 1.查基本信息
            Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
            SkuInfo skuInfo = result.getData();
            detailTo.setSkuInfo(skuInfo);
            return skuInfo;
        }, executor);

        // 2.查商品图片信息
        CompletableFuture<Void> imageFuture = skuInfoFuture.thenAcceptAsync((skuInfo) -> {
            Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
            skuInfo.setSkuImageList(skuImages.getData());
        }, executor);

        // 3.查商品实时价格
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> price = skuDetailFeignClient.getSku1010Price(skuId);
            detailTo.setPrice(price.getData());
        }, executor);

        // 4.查销售属性名和值
        CompletableFuture<Void> saleAttrValueFuture = skuInfoFuture.thenAcceptAsync((skuInfo) -> {
            Result<List<SpuSaleAttr>> skuSaleAttrValues = skuDetailFeignClient
                    .getSkuSaleAttrValues(skuId, skuInfo.getSpuId());
            detailTo.setSpuSaleAttrList(skuSaleAttrValues.getData());
        }, executor);

        // 5.查sku组合
        CompletableFuture<Void> skuValueJsonFuture = skuInfoFuture.thenAcceptAsync((skuInfo) -> {
            Result<String> valueJson = skuDetailFeignClient.getSkuValueJson(skuInfo.getSpuId());
            detailTo.setValuesSkuJson(valueJson.getData());
        }, executor);

        // 6.查分类
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync((skuInfo) -> {
            Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
            detailTo.setCategoryView(categoryView.getData());
        }, executor);
        CompletableFuture
                .allOf(imageFuture, priceFuture, saleAttrValueFuture, skuValueJsonFuture, categoryViewFuture)
                .join();

        return detailTo;
    }


}
