package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-08-25 14:29
 * 分类请求处理器
 * 前后分离：前端发请求，后端处理好响应JSON数据
 * 所有请求全部返回Result对象的JSON，所有要携带的数据都放到Result的data属性
 */
//@Controller       这个类是来接受请求的
//@ResponseBody     所有的相应数据都直接写给浏览器（如果是对象就写成json，如果是文本就写成普通字符串）
@RequestMapping("/admin/product")
@RestController     //上面两个注解的合体
public class CategoryController {

    @Autowired
    BaseCategory1Service baseCategory1Service;

    @Autowired
    BaseCategory2Service baseCategory2Service;

    //获取所有的一级分类
    @GetMapping("/getCategory1")
    public Result getCategory1() {
        List<BaseCategory1> baseCategory1List = baseCategory1Service.list();
        return Result.ok(baseCategory1List);
    }

    //获取某个一级分类下的所有二级分类
    @GetMapping("/getCategory2/{c1id}")
    public Result getCategory2(@PathVariable("c1id") Long c1id) {
        List<BaseCategory2> category2s = baseCategory2Service.getCategory1Child(c1id);
        return Result.ok(category2s);
    }
}
