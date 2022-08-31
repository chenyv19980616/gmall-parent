package com.atguigu.gmall.item;

import com.atguigu.gmall.common.config.Swagger2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @author chenyv
 * @create 2022-08-31 14:46
 */
@Import(Swagger2Config.class)
@EnableFeignClients
@SpringCloudApplication
public class ItemMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemMainApplication.class, args);
    }
}
