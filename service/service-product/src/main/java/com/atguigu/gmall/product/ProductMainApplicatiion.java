package com.atguigu.gmall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author chenyv
 * @create 2022-08-25 13:51
 */
@MapperScan("com.atguigu.gmall.product.mapper")     //让他自动扫描这个包下的所有mapper接口
@SpringCloudApplication
public class ProductMainApplicatiion {
    public static void main(String[] args) {
        SpringApplication.run(ProductMainApplicatiion.class, args);
    }
}
