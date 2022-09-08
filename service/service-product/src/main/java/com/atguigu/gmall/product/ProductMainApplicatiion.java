package com.atguigu.gmall.product;

import com.atguigu.gmall.common.annotation.EnableThreadPool;
import com.atguigu.gmall.common.config.Swagger2Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chenyv
 * @create 2022-08-25 13:51
 */
@EnableScheduling   //开启springboot定时调度功能
@EnableThreadPool
@Import({Swagger2Config.class})
@MapperScan("com.atguigu.gmall.product.mapper")     //让他自动扫描这个包下的所有mapper接口
@SpringCloudApplication
public class ProductMainApplicatiion {
    public static void main(String[] args) {
        SpringApplication.run(ProductMainApplicatiion.class, args);
    }
}
