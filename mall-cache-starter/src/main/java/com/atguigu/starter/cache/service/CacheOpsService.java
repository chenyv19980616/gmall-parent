package com.atguigu.starter.cache.service;

import java.lang.reflect.Type;

/**
 * @author chenyv
 * @create 2022-09-06 14:17
 */
public interface CacheOpsService {

    /**
     * 从缓存中获取一个json并转为普通对象
     *
     * @param cacheKey
     * @param clz
     * @param <T>
     * @return
     */
    <T> T getCacheData(String cacheKey, Class<T> clz);


    /**
     * 从缓存中获取一个json并转为一个复杂对象
     *
     * @param cacheKey
     * @param type
     * @return
     */
    Object getCacheData(String cacheKey, Type type);

    /**
     * 布隆过滤器判断是否有这个商品
     *
     * @param skuId
     * @return
     */
    boolean bloomContains(Object skuId);

    /**
     * 给指定商品加锁
     *
     * @param skuId
     * @return
     */
    boolean tryLock(Long skuId);

    /**
     * 加指定的分布式锁
     *
     * @param lockName
     * @return
     */
    boolean tryLock(String lockName);

    /**
     * 把指定对象使用指定的key保存到redis
     *
     * @param cacheKey
     * @param obj
     */
    void saveData(String cacheKey, Object obj);

    /**
     * 解锁
     *
     * @param skuId
     */
    void unlock(Long skuId);

    /**
     * 判定指定布隆过滤器（bloomName）里面是否包含指定值（bVal）
     *
     * @param bloomName
     * @param bVal
     * @return
     */
    boolean bloomContains(String bloomName, Object bVal);

    /**
     * 解指定的锁
     *
     * @param lockName
     */
    void unlock(String lockName);
}
