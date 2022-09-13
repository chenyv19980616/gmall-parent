package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author chenyv
 * @create 2022-09-13 10:46
 */
@SpringBootTest
public class ReadWriteSpliteTest {

    @Resource
    BaseTrademarkMapper baseTrademarkMapper;

    @Test
    void testRW() {
        BaseTrademark baseTrademark = baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark = " + baseTrademark);
    }
}
