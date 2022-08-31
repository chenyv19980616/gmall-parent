package com.atgugiu.gmall.web.controller;

import com.atgugiu.gmall.web.feign.CategoryFeignClient;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-08-30 16:31
 */
@Controller
public class IndexController {

    @Autowired
    CategoryFeignClient categoryFeignClient;

    /**
     * 跳转首页
     * @return
     */
    @GetMapping({"/", "/index"})
    public String indexPage(Model model) {
        Result<List<CategoryTreeTo>> result = categoryFeignClient.getAllCategorysWithTree();
        if (result.getCode() == 200) {
            List<CategoryTreeTo> data = result.getData();
            model.addAttribute("list", data);
        }
        return "index/index";
    }
}
