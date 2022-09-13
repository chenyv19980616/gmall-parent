package com.atguigu.gmall.common.retry;

import feign.RetryableException;
import feign.Retryer;

/**
 * @author chenyv
 * @create 2022-09-13 14:10
 * 自定义feign重试次数的逻辑
 */
public class MyRetry implements Retryer {

    private int cur;
    private int max;

    public MyRetry() {
        cur = 0;
        max = 2;
    }

    /**
     * 继续重试还是中断重试
     * @param e
     */
    @Override
    public void continueOrPropagate(RetryableException e) {
        throw e;
    }

    @Override
    public Retryer clone() {
        return this;
    }
}
