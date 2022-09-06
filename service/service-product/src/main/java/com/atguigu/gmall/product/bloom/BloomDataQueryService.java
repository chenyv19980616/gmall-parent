package com.atguigu.gmall.product.bloom;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-09-06 19:57
 * 布隆数据查询服务
 */
public interface BloomDataQueryService {

    /**
     * 模板模式
     * @return
     */
    List queryData();
}
