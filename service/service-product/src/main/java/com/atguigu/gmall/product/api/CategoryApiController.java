package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-08-30 16:48
 */
@RestController
@Api(tags = "分类有关的API")
@RequestMapping("/api/inner/rpc/product")
public class CategoryApiController {

    @Autowired
    BaseCategory1Service baseCategory1Service;

    @ApiOperation("查询所有分类并封装成树形结构")
    @GetMapping("/category/tree")
    public Result getAllCategorysWithTree() {
        List<CategoryTreeTo> categoryTreeTos = baseCategory1Service.getAllCategorysWithTree();
        return Result.ok(categoryTreeTos);
    }
}

