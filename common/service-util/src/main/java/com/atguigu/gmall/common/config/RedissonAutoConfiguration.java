package com.atguigu.gmall.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author chenyv
 * @create 2022-09-06 10:35
 * 1. 导入redisson的jar包
 * 2. 给容器中放好 RedissonClient 的组件。
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedissonAutoConfiguration {

    @Resource
    RedisProperties redisProperties;

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
