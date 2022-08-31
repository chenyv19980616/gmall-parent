package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.product.mapper.BaseCategory1Mapper;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenyv
 * @description 针对表【base_category1(一级分类表)】的数据库操作Service实现
 * @createDate 2022-08-30 17:05:31
 */
@Service
public class BaseCategory1ServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1>
        implements BaseCategory1Service {

    @Resource
    BaseCategory1Mapper category1Mapper;

    @Override
    public List<CategoryTreeTo> getAllCategorysWithTree() {
        List<CategoryTreeTo> categoryTreeTos = category1Mapper.getAllCategorysWithTree();
        return categoryTreeTos;
    }
}




