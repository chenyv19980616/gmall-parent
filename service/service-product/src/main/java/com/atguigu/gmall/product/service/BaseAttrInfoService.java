package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author chenyv
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2022-08-26 16:46:50
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    /**
     *
     * @param c1Id  一级分类id
     * @param c2Id  二级分类id
     * @param c3Id  三级分类id
     * @return
     */
    List<BaseAttrInfo> getAttrInfoAndValueByCategoryId(Long c1Id, Long c2Id, Long c3Id);

    //保存平台属性值
    void saveAttrInfo(BaseAttrInfo info);
}
