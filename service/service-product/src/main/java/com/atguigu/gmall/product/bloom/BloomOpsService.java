package com.atguigu.gmall.product.bloom;

/**
 * @author chenyv
 * @create 2022-09-06 19:25
 */
public interface BloomOpsService {
    /**
     * 重建指定布隆
     * @param bloomName
     */
    void rebuildBloom(String bloomName,BloomDataQueryService dataQueryService);
}
