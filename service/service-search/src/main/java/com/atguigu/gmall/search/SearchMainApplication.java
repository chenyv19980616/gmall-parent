package com.atguigu.gmall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author chenyv
 * @create 2022-09-14 10:55
 */
@SpringCloudApplication
@EnableElasticsearchRepositories    //开启ES的自动仓库功能
public class SearchMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchMainApplication.class, args);
    }
}
