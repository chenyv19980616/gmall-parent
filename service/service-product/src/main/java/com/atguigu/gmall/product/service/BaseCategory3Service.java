package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategory3;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author chenyv
* @description 针对表【base_category3(三级分类表)】的数据库操作Service
* @createDate 2022-08-25 16:16:15
*/
public interface BaseCategory3Service extends IService<BaseCategory3> {

    //获取某个二级分类下的所有三级分类
    List<BaseCategory3> getCategory2Child(Long c2id);

}
