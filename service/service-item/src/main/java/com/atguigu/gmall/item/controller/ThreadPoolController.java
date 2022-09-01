package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chenyv
 * @create 2022-09-01 17:01
 */
@RestController
public class ThreadPoolController {

    @Autowired
    ThreadPoolExecutor executor;

    @GetMapping("/close/pool")
    public Result closePool() {
        executor.shutdown();
        return Result.ok();
    }

    @GetMapping("/monitor/pool")
    public Result monitorThreadPool() {
        int corePoolSize = executor.getCorePoolSize();
        long taskCount = executor.getTaskCount();
        return Result.ok(corePoolSize + "============" + taskCount);
    }
}
