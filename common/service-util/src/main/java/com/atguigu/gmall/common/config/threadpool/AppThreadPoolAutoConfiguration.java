package com.atguigu.gmall.common.config.threadpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenyv
 * @create 2022-09-01 15:23
 * 配置线程池
 */
//开启自动化属性化绑定配置
@Configuration
@EnableConfigurationProperties(AppThreadPoolProperties.class)
public class AppThreadPoolAutoConfiguration {

    @Value("${spring.application.name}")
    String applicationName;

    @Autowired
    AppThreadPoolProperties appThreadPoolProperties;

    @Bean
    public ThreadPoolExecutor coreExecutor() {
        /**
         * 参数：
         * int corePoolSize:核心线程池: cpu核心数
         * int maximumPoolSize:最大线程数
         * long keepAliveTime:时间长度
         * TimeUnit unit:时间单位
         * BlockingQueue<Runnable> workQueue:阻塞队列
         * ThreadFactory threadFactory:线程工厂
         * RejectedExecutionHandler handler:拒绝策略
         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                appThreadPoolProperties.getCore(),
                appThreadPoolProperties.getMax(),
                appThreadPoolProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(appThreadPoolProperties.getQueueSize()),
                new ThreadFactory() {
                    int i = 0;

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(applicationName + "[core-thread-" + i++ + "]");
                        return thread;
                    }
                },
                //生产环境用CallerRunPolicy，保证就算线程池满了
                //不能提交的任务，有当前线程自己以同步的方式进行
                new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
