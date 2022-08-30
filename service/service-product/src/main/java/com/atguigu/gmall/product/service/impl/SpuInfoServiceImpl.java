package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenyv
 * @description 针对表【spu_info(商品表)】的数据库操作Service实现
 * @createDate 2022-08-26 16:46:50
 */
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
        implements SpuInfoService {

    @Resource
    SpuInfoMapper spuInfoMapper;

    @Autowired
    SpuImageService spuImageService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    SpuSaleAttrValueService spuSaleAttrValueService;

    @Override
    @Transactional
    public void saveSpuInfo(SpuInfo spuInfo) {
        // 1.把spu基本信息保存到spu_info表中
        spuInfoMapper.insert(spuInfo);
        Long id = spuInfo.getId();

        // 2.把spu的图片存到spu_image表
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        for (SpuImage image : spuImageList) {
            image.setSpuId(id);
        }
        //批量保存图片
        spuImageService.saveBatch(spuImageList);

        // 3.保存销售属性名 到spu_sale_attr
        List<SpuSaleAttr> attrNameList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr attr : attrNameList) {
            attr.setSpuId(id);
            // 4.拿到这个属性名对应的所有销售属性值集合
            List<SpuSaleAttrValue> valueList = attr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue value : valueList) {
                value.setSpuId(id);
                String saleAttrName = attr.getSaleAttrName();
                value.setSaleAttrName(saleAttrName);
            }
            spuSaleAttrValueService.saveBatch(valueList);
        }
        spuSaleAttrService.saveBatch(attrNameList);
    }
}




