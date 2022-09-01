package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author chenyv
* @description 针对表【base_category1(一级分类表)】的数据库操作Service
* @createDate 2022-08-30 17:05:31
*/
public interface BaseCategory1Service extends IService<BaseCategory1> {

    /**
     * 查询分类以及他下面的子分类，并组装成树形结构
     * @return
     */
    List<CategoryTreeTo> getAllCategorysWithTree();

    /**
     * 根据三级分类id，查询出整个精确路径
     * @param c3Id
     * @return
     */
    CategoryViewTo getCategoryView(Long c3Id);
}
