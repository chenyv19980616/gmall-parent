package com.atguigu.gmall.product.schedule;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author chenyv
 * @create 2022-09-06 19:54
 * 重建布隆任务
 */
@Service
public class RebuildBloomTask {
    @Autowired
    BloomOpsService bloomOpsService;

    @Autowired
    BloomDataQueryService bloomDataQueryService;

    //每隔7天重建一次；
    @Scheduled(cron = "0 0 3 ? * 3")
    public void rebuildBloom() {
        bloomOpsService.rebuildBloom(SysRedisConst.BLOOM_SKUID, bloomDataQueryService);

    }
}
