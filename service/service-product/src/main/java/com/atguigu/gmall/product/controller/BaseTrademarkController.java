package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-08-27 16:32
 */
@Api(tags = "品牌的API")
@RequestMapping("/admin/product")
@RestController
public class BaseTrademarkController {

    @Autowired
    BaseTrademarkService baseTrademarkService;

    @ApiOperation("分页查询所有品牌")
    @GetMapping("/baseTrademark/{pn}/{size}")
    public Result baseTrademark(@PathVariable("pn") Long pn, @PathVariable("size") Long size) {
        Page<BaseTrademark> page = new Page<>(pn, size);
        Page<BaseTrademark> baseTrademarkPage = baseTrademarkService.page(page);
        return Result.ok(baseTrademarkPage);
    }

    @ApiOperation("根据品牌id获取品牌信息")
    @GetMapping("/baseTrademark/get/{id}")
    public Result baseTrademark(@PathVariable("id") Long id) {
        BaseTrademark trademark = baseTrademarkService.getById(id);
        return Result.ok(trademark);
    }

    @ApiOperation("根据品牌id修改品牌信息")
    @PutMapping("/baseTrademark/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    @ApiOperation("新增品牌")
    @PostMapping("/baseTrademark/save")
    public Result baseBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    @ApiOperation("删除品牌")
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result removebaseTrademark(@PathVariable("id") Long id) {
        baseTrademarkService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("获取所有品牌")
    @GetMapping("/baseTrademark/getTrademarkList")
    public Result getTrademarkList() {
        List<BaseTrademark> list = baseTrademarkService.list();
        return Result.ok(list);
    }
}
