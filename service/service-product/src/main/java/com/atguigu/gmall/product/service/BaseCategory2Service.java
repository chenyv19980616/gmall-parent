package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategory2;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author chenyv
* @description 针对表【base_category2(二级分类表)】的数据库操作Service
* @createDate 2022-08-25 16:16:15
*/
public interface BaseCategory2Service extends IService<BaseCategory2> {

    /**
     * 查询一级分类下的二级分类
     * @param c1id
     * @return
     */
    List<BaseCategory2> getCategory1Child(Long c1id);
}
