package com.atguigu.gmall.product.bloom.impl;

import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-09-06 19:59
 */
@Service
public class SkuBloomDataQueryServiceImpl implements BloomDataQueryService {

    @Autowired
    SkuInfoService skuInfoService;

    @Override
    public List queryData() {
        List<Long> allSkuId = skuInfoService.findAllSkuId();
        return allSkuId;
    }
}
