package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.product.SkuProductFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.starter.cache.annotation.GmallCache;
import com.atguigu.starter.cache.service.CacheOpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chenyv
 * @create 2022-08-31 16:50
 */
@Slf4j
@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Resource
    SkuProductFeignClient skuProductFeignClient;

    @Resource
    ThreadPoolExecutor executor;

    @Autowired
    CacheOpsService cacheOpsService;

    /**
     * 表达式中的params代表方法的所有参数列表
     *
     * @param skuId
     * @return
     */
    @GmallCache(
            cacheKey = SysRedisConst.SKU_INFO_PREFIX + "#{#params[0]}",
            bloomName = SysRedisConst.BLOOM_SKUID,
            bloomValue = "#{#params[0]}",
            lockName = SysRedisConst.LOCK_SKU_DETAIL + "#{#params[0]}",
            ttl = 60 * 60 * 24 * 7L)
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
        return fromRpc;
    }

    public SkuDetailTo getSkuDetailWithCache(Long skuId) {
        String cacheKey = SysRedisConst.SKU_INFO_PREFIX + skuId;
        // 1. 先查缓存
        SkuDetailTo cacheData = cacheOpsService.getCacheData(cacheKey, SkuDetailTo.class);
        // 2. 判断
        if (cacheData == null) {
            // 3. 缓存没有
            // 3.1 先问布隆过滤器，是否有这个商品
            boolean isContains = cacheOpsService.bloomContains(skuId);
            if (!isContains) {
                // 布隆说没有，一定没有
                log.info("[{}]商品 - 布隆判定没有，检测到隐藏的攻击属性！", skuId);
                return null;
            }
            // 布隆说有，有可能有，就需要回源查数据
            // 为当前商品加自己的分布式锁
            boolean lock = cacheOpsService.tryLock(skuId);
            if (lock) {
                //获取锁成功,查询远程
                log.info("[{}]缓存未命中，布隆说有，准备回源。。。", skuId);
                SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
                //数据放缓存
                cacheOpsService.saveData(cacheKey, fromRpc);
                //解锁
                cacheOpsService.unlock(skuId);
                return fromRpc;
            }
            //没获取到锁
            try {
                Thread.sleep(1000);
                return cacheOpsService.getCacheData(cacheKey, SkuDetailTo.class);
            } catch (InterruptedException e) {

            }
        }
        // 4. 缓存中有
        return cacheData;
    }

    //未优化缓存前
    public SkuDetailTo getSkuDetailFromRpc(Long skuId) {
        SkuDetailTo detailTo = new SkuDetailTo();

        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            // 1.查基本信息
            Result<SkuInfo> result = skuProductFeignClient.getSkuInfo(skuId);
            SkuInfo skuInfo = result.getData();
            detailTo.setSkuInfo(skuInfo);
            return skuInfo;
        }, executor);

        // 2.查商品图片信息
        CompletableFuture<Void> imageFuture = skuInfoFuture.thenAcceptAsync((skuInfo) -> {
            if (skuInfo != null) {
                Result<List<SkuImage>> skuImages = skuProductFeignClient.getSkuImages(skuId);
                skuInfo.setSkuImageList(skuImages.getData());
            }
        }, executor);

        // 3.查商品实时价格
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> price = skuProductFeignClient.getSku1010Price(skuId);
            detailTo.setPrice(price.getData());
        }, executor);

        // 4.查销售属性名和值
        CompletableFuture<Void> saleAttrValueFuture = skuInfoFuture.thenAcceptAsync((skuInfo) -> {
            if (skuInfo != null) {
                Result<List<SpuSaleAttr>> skuSaleAttrValues = skuProductFeignClient
                        .getSkuSaleAttrValues(skuId, skuInfo.getSpuId());
                detailTo.setSpuSaleAttrList(skuSaleAttrValues.getData());
            }
        }, executor);

        // 5.查sku组合
        CompletableFuture<Void> skuValueJsonFuture = skuInfoFuture.thenAcceptAsync((skuInfo) -> {
            if (skuInfo != null) {
                Result<String> valueJson = skuProductFeignClient.getSkuValueJson(skuInfo.getSpuId());
                detailTo.setValuesSkuJson(valueJson.getData());
            }
        }, executor);

        // 6.查分类
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync((skuInfo) -> {
            if (skuInfo != null) {
                Result<CategoryViewTo> categoryView = skuProductFeignClient.getCategoryView(skuInfo.getCategory3Id());
                detailTo.setCategoryView(categoryView.getData());
            }
        }, executor);
        CompletableFuture
                .allOf(imageFuture, priceFuture, saleAttrValueFuture, skuValueJsonFuture, categoryViewFuture)
                .join();

        return detailTo;
    }



    /*
    private Map<Long, SkuDetailTo> skuCache = new ConcurrentHashMap<>();        //本地缓存容器

    @Autowired
    StringRedisTemplate redisTemplate;      //Redis缓存容器

    public SkuDetailTo getSkuDetailXxxxFeature(Long skuId) {
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

    //缓存优化后使用本地缓存
    public SkuDetailTo getSkuDetailbendi(Long skuId) {

        // 1.先看缓存
        SkuDetailTo cacheData = skuCache.get(skuId);
        // 2.判断
        if (cacheData == null) {
            // 3.缓存没有,真正查询【回源（回到数据源头真正检索）】【提高缓存命中率】
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            skuCache.put(skuId, fromRpc);
            return fromRpc;
        }
        return cacheData;
    }
    */

}
