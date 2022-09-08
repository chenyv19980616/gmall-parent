package com.atguigu.gmall.item.cache.impl;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.cache.CacheOpsService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * @author chenyv
 * @create 2022-09-06 14:18
 * 封装了缓存操作
 */
@Service
public class CacheOpsServiceImpl implements CacheOpsService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    /**
     * 从redis缓存中获取一个数据，并且转成指定的类型的对象
     *
     * @param cacheKey
     * @param clz
     * @param <T>
     * @return
     */
    @Override
    public <T> T getCacheData(String cacheKey, Class<T> clz) {
        String jsonStr = redisTemplate.opsForValue().get(cacheKey);
        if (SysRedisConst.NULL_VALUE.equals(jsonStr)) {
            return null;
        }
        T t = Jsons.toObj(jsonStr, clz);
        return t;
    }

    @Override
    public Object getCacheData(String cacheKey, Type type) {
        String jsonStr = redisTemplate.opsForValue().get(cacheKey);
        if (SysRedisConst.NULL_VALUE.equals(jsonStr)) {
            return null;
        }
        //逆转Json为Type类型的复杂对象
        Object obj = Jsons.toObj(jsonStr, new TypeReference<Object>() {
            @Override
            public Type getType() {
                return type;    //这个是方法的带泛型的返回值类型
            }
        });
        return obj;
    }

    @Override
    public boolean bloomContains(Object skuId) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);
        return bloomFilter.contains(skuId);
    }

    @Override
    public boolean tryLock(Long skuId) {
        String lockKey = SysRedisConst.LOCK_SKU_DETAIL + skuId;
        RLock lock = redissonClient.getLock(lockKey);
        boolean tryLock = lock.tryLock();
        return tryLock;
    }

    @Override
    public void saveData(String cacheKey, Object obj) {
        if (obj == null) {
            //空值时间短一点
            redisTemplate.opsForValue().set(cacheKey, SysRedisConst.NULL_VALUE, SysRedisConst.NULL_VAL_TTL, TimeUnit.SECONDS);
        } else {
            String s = Jsons.toStr(obj);
            redisTemplate.opsForValue().set(cacheKey, s, SysRedisConst.SKUDETAIL_TTL, TimeUnit.SECONDS);
        }
    }

    @Override
    public void unlock(Long skuId) {
        String lockKey = SysRedisConst.LOCK_SKU_DETAIL + skuId;
        RLock lock = redissonClient.getLock(lockKey);
        //解掉这把锁
        lock.unlock();
    }
}
