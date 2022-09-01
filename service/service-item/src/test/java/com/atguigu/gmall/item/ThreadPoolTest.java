package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chenyv
 * @create 2022-09-01 16:15
 */
@SpringBootTest
public class ThreadPoolTest {

    @Autowired
    ThreadPoolExecutor executor;

    @Test
    void testPool() {
        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + "haha");
        });
    }
}
