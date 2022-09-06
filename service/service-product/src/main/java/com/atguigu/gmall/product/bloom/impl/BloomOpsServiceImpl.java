package com.atguigu.gmall.product.bloom.impl;

import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import com.atguigu.gmall.product.service.SkuInfoService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-09-06 19:25
 */
@Service
public class BloomOpsServiceImpl implements BloomOpsService {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SkuInfoService skuInfoService;

    /**
     * @param bloomName
     */
    @Override
    public void rebuildBloom(String bloomName, BloomDataQueryService dataQueryService) {
        RBloomFilter<Object> oldBloomFilter = redissonClient.getBloomFilter(bloomName);
        // 1.先准备一个新的布隆过滤器，所有的东西都初始化好
        String newBloomName = bloomName + "_new";
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(newBloomName);
        // 2.拿到所有商品id
//        List<Long> allSkuId = skuInfoService.findAllSkuId();
        List list = dataQueryService.queryData();   //动态决定
        // 3.初始化新的布隆过滤器
        bloomFilter.tryInit(5000000, 0.00001);
        // 4.给布隆过滤器添加数据
        for (Object o : list) {
            bloomFilter.add(o);
        }
        // 5.新布隆准备就绪
        // ob bb nb

        // 6.两个交换
        oldBloomFilter.rename("bbb_bloom");   //老布隆下线
        bloomFilter.rename(bloomName);  //新布隆上线

        // 7.删除老布隆和中间交换布隆
        oldBloomFilter.deleteAsync();
        redissonClient.getBloomFilter("bbb_bloom");
    }
}
