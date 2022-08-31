package com.atguigu.gmall.item.service;

import com.atguigu.gmall.model.to.SkuDetailTo;

/**
 * @author chenyv
 * @create 2022-08-31 16:50
 */
public interface SkuDetailService {
    SkuDetailTo getSkuDetail(Long skuId);
}
