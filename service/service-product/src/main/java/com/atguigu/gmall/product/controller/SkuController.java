package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenyv
 * @create 2022-08-29 21:01
 */
@Api(tags = "SKU管理")
@RestController
@RequestMapping("/admin/product")
public class SkuController {

    @Autowired
    SkuInfoService skuInfoService;

    @ApiOperation("SKU分页查询")
    @GetMapping("/list/{pn}/{ps}")
    public Result list(@PathVariable("pn") Long pn, @PathVariable("ps") Long ps) {
        Page<SkuInfo> page = new Page<>(pn, ps);
        Page<SkuInfo> skuInfoPage = skuInfoService.page(page);
        return Result.ok(skuInfoPage);
    }

    @ApiOperation("保存SKU信息")
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo info) {
        skuInfoService.saveSkuInfo(info);
        return Result.ok();
    }

    @ApiOperation("商品上架")
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.onSale(skuId);
        return Result.ok();
    }

    @ApiOperation("商品下架")
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.cancelSale(skuId);
        return Result.ok();
    }

    /**
     * 修改sky信息
     */
    @PostMapping("/update/skuId")
    public Result updateSkuInfo(SkuInfo skuInfo) {
//        skuInfoService.updateSkuInfo();
//        cacheOpsService.delay2Delete(skuInfo.getId());
        return Result.ok();
    }
}
