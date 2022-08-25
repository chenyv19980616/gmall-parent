package com.atguigu.gmall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author chenyv
 * @create 2022-08-24 16:13
 * 主启动类
 */

//@EnableCircuitBreaker   //开启服务熔断降级，流量保护功能【1.导入jar包spring-cloud-starter-alibaba-sentinel  2.使用注解】
//@EnableDiscoveryClient  //开启服务发现【1.导入服务发现jar包spring-cloud-starter-alibaba-nacos-discovery   2.使用注解】
//@SpringBootApplication

@SpringCloudApplication     //以上的合体
public class GatewayMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayMainApplication.class, args);
    }
}
