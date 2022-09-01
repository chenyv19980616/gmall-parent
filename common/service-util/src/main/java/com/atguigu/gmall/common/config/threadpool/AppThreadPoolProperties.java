package com.atguigu.gmall.common.config.threadpool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chenyv
 * @create 2022-09-01 15:56
 */
@Data
@ConfigurationProperties(prefix = "app.thread-pool")
public class AppThreadPoolProperties {
    /**
     * core: 2
     * max: 4
     * queue-size: 1000
     * keep-alive-time: 300
     */
    Integer core = 2;

    Integer max = 4;

    Integer queueSize = 200;

    Long keepAliveTime = 300L;
}
