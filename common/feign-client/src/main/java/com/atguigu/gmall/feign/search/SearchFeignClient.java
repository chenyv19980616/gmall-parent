package com.atguigu.gmall.feign.search;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.SearchParamVo;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenyv
 * @create 2022-09-15 15:28
 */
@FeignClient("service-search")
@RequestMapping("/api/inner/rpc/search")
public interface SearchFeignClient {
    @PostMapping("/goods")
    Result saveGoods(@RequestBody Goods goods);

    @DeleteMapping("/goods/{skuId}")
    Result deleteGoods(@PathVariable("skuId") Long skuId);

    @PostMapping("/goods/search")
    Result<SearchResponseVo> search(@RequestBody SearchParamVo paramVo);
}
