package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-08-26 16:33
 * 平台属性相关API
 */
@RequestMapping("/admin/product")
@RestController
public class BaseAttrController {

    @Autowired
    BaseAttrInfoService baseAttrInfoService;

    @Autowired
    BaseAttrValueService baseAttrValueService;

    //查询某个分类下的所有平台属性
    @GetMapping("/attrInfoList/{c1Id}/{c2Id}/{c3Id}")
    public Result getAttrInfoList(@PathVariable("c1Id") Long c1Id,
                                  @PathVariable("c2Id") Long c2Id,
                                  @PathVariable("c3Id") Long c3Id) {
        List<BaseAttrInfo> infos = baseAttrInfoService.getAttrInfoAndValueByCategoryId(c1Id, c2Id, c3Id);

        return Result.ok(infos);
    }

    //保存和修改属性信息二合一的方法
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo info) {
        baseAttrInfoService.saveAttrInfo(info);
        return Result.ok();
    }

    //根据平台属性id获取属性信息(属性名，属性值)
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId") Long attrId) {
        List<BaseAttrValue> list = baseAttrValueService.getAttrValueList(attrId);
        return Result.ok(list);
    }

}
