package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author chenyv
* @description 针对表【base_category1(一级分类表)】的数据库操作Mapper
* @createDate 2022-08-30 17:05:31
* @Entity com.atguigu.gmall.product.domain.BaseCategory1
*/
public interface BaseCategory1Mapper extends BaseMapper<BaseCategory1> {

    List<CategoryTreeTo> getAllCategorysWithTree();

    //根据三级分类id查询对应的一级，二级父分类
    CategoryViewTo getCategoryView(@Param("category3Id") Long category3Id);
}




