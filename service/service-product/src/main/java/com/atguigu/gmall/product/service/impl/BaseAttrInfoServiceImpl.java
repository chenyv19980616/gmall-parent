package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenyv
 * @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
 * @createDate 2022-08-26 16:46:50
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
        implements BaseAttrInfoService {

    @Resource
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Resource
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrInfoAndValueByCategoryId(Long c1Id, Long c2Id, Long c3Id) {
        List<BaseAttrInfo> infos = baseAttrInfoMapper.getAttrInfoAndValueByCategoryId(c1Id, c2Id, c3Id);
        return infos;
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo info) {
        //id为null时新增，id不为null时修改:
        if (info.getId() == null) {
            //1.新增
            addBaseAttrInfo(info);
        } else {
            //2.修改
            updateBaseAttrInfo(info);
        }
    }

    /*修改属性信息*/
    private void updateBaseAttrInfo(BaseAttrInfo info) {
        //2.1更改属性名信息
        baseAttrInfoMapper.updateById(info);
        //2.2更改属性值信息
        List<BaseAttrValue> valueList = info.getAttrValueList();
        //2.2.1先删除要删除的（没有提交的）
        //收集前端提交的所有属性值id
        List<Long> vids = new ArrayList<>();
        for (BaseAttrValue attrValue : valueList) {
            Long id = attrValue.getId();
            if (id != null) {
                vids.add(id);
            }
        }
        if (vids.size() > 0) {
            //部分删除
            QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("attr_id", info.getId());
            deleteWrapper.notIn("id", vids);
            baseAttrValueMapper.delete(deleteWrapper);
        } else {
            //全部删除
            QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("attr_id", info.getId());
            baseAttrValueMapper.delete(deleteWrapper);
        }

        for (BaseAttrValue attrValue : valueList) {
            if (attrValue.getId() != null) {
                //2.2.2属性值有id，此次需要修改
                baseAttrValueMapper.updateById(attrValue);
            } else if (attrValue.getId() == null) {
                //2.2.3属性值没有id，此次需要新增
                attrValue.setAttrId(info.getId());
                baseAttrValueMapper.insert(attrValue);
            }
        }
    }

    /*新增属性信息*/
    private void addBaseAttrInfo(BaseAttrInfo info) {
        //1. 保存属性名
        baseAttrInfoMapper.insert(info);
        Long id = info.getId();
        //2. 保存属性值
        List<BaseAttrValue> valueList = info.getAttrValueList();
        for (BaseAttrValue baseAttrValue : valueList) {
            //回填属性名记录的自增id
            baseAttrValue.setAttrId(id);
            baseAttrValueMapper.insert(baseAttrValue);
        }
    }
}




