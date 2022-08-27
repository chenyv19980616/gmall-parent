package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenyv
 * @description 针对表【base_attr_value(属性值表)】的数据库操作Service实现
 * @createDate 2022-08-26 16:46:50
 */
@Service
public class BaseAttrValueServiceImpl extends ServiceImpl<BaseAttrValueMapper, BaseAttrValue>
        implements BaseAttrValueService {

    @Resource
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id", attrId);
        List<BaseAttrValue> valueList = baseAttrValueMapper.selectList(wrapper);
        return valueList;
    }
}




