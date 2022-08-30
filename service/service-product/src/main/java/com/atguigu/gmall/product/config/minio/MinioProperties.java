package com.atguigu.gmall.product.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chenyv
 * @create 2022-08-29 12:32
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.minio")
//和配置文件绑定
public class MinioProperties {
    String endpoint;
    String ak;
    String sk;
    String bucketName;
}
