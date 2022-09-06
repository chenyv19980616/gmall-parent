package com.atguigu.gmall.product.init;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.product.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author chenyv
 * @create 2022-09-06 16:28
 * 容器启动成功后，连上数据库，查到所有商品id，在布隆过滤器里面占位
 */
@Slf4j
@Service
public class SkuIdBloomInitService {

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    RedissonClient redissonClient;

    @PostConstruct  //当前组件创建成功后就执行
    public void initSkuBloom() {
        log.info("布隆初始化正在进行。。。");
        // 1.查询出所有的skuId
        List<Long> skuIds = skuInfoService.findAllSkuId();

        // 2.把所有的id初始化到布隆过滤器中
        RBloomFilter<Object> bloomFilter =
                redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);
        // 3.初始化布隆过滤器
        // long expectedInsertions：期望插入的数据量
        // double falseProbability：容错率（误判率）
        boolean exists = bloomFilter.isExists();
        if (!exists) {
            // 尝试初始化。如果布隆过滤器没有初始化过，就尝试初始化
            bloomFilter.tryInit(5000000, 0.00001);
        }
        // 4.把所有的商品添加到布隆中
        for (Long skuId : skuIds) {
            bloomFilter.add(skuId);
        }
        log.info("布隆初始化完成，总计添加了 {} 条数据",skuIds.size());
    }
}
