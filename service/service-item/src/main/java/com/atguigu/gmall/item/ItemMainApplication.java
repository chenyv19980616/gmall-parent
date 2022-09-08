package com.atguigu.gmall.item;

import com.atguigu.gmall.common.annotation.EnableThreadPool;
import com.atguigu.gmall.common.config.RedissonAutoConfiguration;
import com.atguigu.gmall.common.config.Swagger2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author chenyv
 * @create 2022-08-31 14:46
 */
@EnableAspectJAutoProxy //开启aspectj的自动代理功能
@EnableThreadPool
@EnableFeignClients
@SpringCloudApplication
@Import({Swagger2Config.class, RedissonAutoConfiguration.class})
public class ItemMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemMainApplication.class, args);
    }
}
