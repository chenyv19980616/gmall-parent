package com.atguigu.gmall.product;

import com.atguigu.gmall.common.config.Swagger2Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Import;

/**
 * @author chenyv
 * @create 2022-08-25 13:51
 */
@Import(Swagger2Config.class)
@MapperScan("com.atguigu.gmall.product.mapper")     //让他自动扫描这个包下的所有mapper接口
@SpringCloudApplication
public class ProductMainApplicatiion {
    public static void main(String[] args) {
        SpringApplication.run(ProductMainApplicatiion.class, args);
    }
}
