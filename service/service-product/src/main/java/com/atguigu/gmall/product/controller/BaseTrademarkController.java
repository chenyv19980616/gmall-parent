package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenyv
 * @create 2022-08-27 16:32
 * 品牌的API
 */
@RequestMapping("/admin/product")
@RestController
public class BaseTrademarkController {

    @Autowired
    BaseTrademarkService baseTrademarkService;

    //分页查询所有品牌
    @GetMapping("/baseTrademark/{pn}/{size}")
    public Result baseTrademark(@PathVariable("pn") Long pn, @PathVariable("size") Long size) {
        Page<BaseTrademark> page = new Page<>(pn, size);
        Page<BaseTrademark> baseTrademarkPage = baseTrademarkService.page(page);
        return Result.ok(baseTrademarkPage);
    }

    //根据品牌id获取品牌信息
    @GetMapping("/baseTrademark/get/{id}")
    public Result baseTrademark(@PathVariable("id") Long id) {
        BaseTrademark trademark = baseTrademarkService.getById(id);
        return Result.ok(trademark);
    }

    //根据品牌id修改品牌信息
    @PutMapping("/baseTrademark/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    //新增品牌
    @PostMapping("/baseTrademark/save")
    public Result baseBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    //删除品牌
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result removebaseTrademark(@PathVariable("id") Long id) {
        baseTrademarkService.removeById(id);
        return Result.ok();
    }
}
