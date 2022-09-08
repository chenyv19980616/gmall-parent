package com.atguigu.starter.cache;

import com.atguigu.starter.cache.aspect.CacheAspect;
import com.atguigu.starter.cache.service.CacheOpsService;
import com.atguigu.starter.cache.service.impl.CacheOpsServiceImpl;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.Resource;

/**
 * @author chenyv
 * @create 2022-09-08 14:52
 * 以前容器中的所有组件要到导入进去
 */
@Configuration
@EnableAspectJAutoProxy
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class MallCacheAutoConfiguration {

    @Resource
    RedisProperties redisProperties;

    @Bean
    public CacheAspect cacheAspect() {
        return new CacheAspect();
    }

    @Bean
    public CacheOpsService cacheOpsService() {
        return new CacheOpsServiceImpl();
    }

    @Bean
    public RedissonClient redissonClient() {
        //1. 创建一个配置
        Config config = new Config();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        //2. 指定好Redisson的配置项
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        //3. 创建一个 RedissonClient
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}

