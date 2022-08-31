package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory1Mapper;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenyv
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2022-08-26 16:46:50
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
        implements SkuInfoService {

    @Autowired
    SkuImageService skuImageService;

    @Autowired
    SkuAttrValueService skuAttrValueService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    SkuInfoMapper skuInfoMapper;

    @Resource
    BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Transactional
    @Override
    public void saveSkuInfo(SkuInfo info) {
        // 1.sku基本信息保存到 sku_info
        save(info);
        Long skuId = info.getId();

        // 2.sku图片信息保存到 sku_image
        List<SkuImage> skuImageList = info.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuId);
        }
        skuImageService.saveBatch(skuImageList);

        // 3.sku的平台属性名和值的关系保存到 sku_attr_value
        List<SkuAttrValue> attrValueList = info.getSkuAttrValueList();
        for (SkuAttrValue attrValue : attrValueList) {
            attrValue.setSkuId(skuId);
        }
        skuAttrValueService.saveBatch(attrValueList);

        // 4.sku的销售属性名和值的关系保存到  sku_sale_attr_value
        List<SkuSaleAttrValue> saleAttrValueList = info.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue saleAttrValue : saleAttrValueList) {
            saleAttrValue.setSkuId(skuId);
            saleAttrValue.setSpuId(info.getSpuId());
        }
        skuSaleAttrValueService.saveBatch(saleAttrValueList);
    }

    @Override
    public void cancelSale(Long skuId) {
        //修改数据库 sku_info 这个skuId的is_sale：1 上架   0 下架
        skuInfoMapper.updateIsSale(skuId, 0);

    }

    @Override
    public void onSale(Long skuId) {
        skuInfoMapper.updateIsSale(skuId, 1);
    }

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {

        SkuDetailTo skuDetailTo = new SkuDetailTo();

        //获取sku的基本信息（信息里面包括 category3_id）
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        //商品sku的图片信息
        skuInfo.setSkuImageList(skuImageService.getSkuImage(skuId));

        skuDetailTo.setSkuInfo(skuInfo);

        //商品sku的完整分类信息
        CategoryViewTo categoryViewTo = baseCategory1Mapper.getCategoryView(skuInfo.getCategory3Id());
        skuDetailTo.setCategoryView(categoryViewTo);

        //实时价格查询
        BigDecimal price = get1010price(skuId);
        skuDetailTo.setPrice(price);

        List<SpuSaleAttr> saleAttrList = spuSaleAttrService.getSaleAttrAndValueMarkSku(skuInfo.getSpuId(),skuId);
        skuDetailTo.setSpuSaleAttrList(saleAttrList);

        return skuDetailTo;
    }

    @Override
    public BigDecimal get1010price(Long skuId) {
        BigDecimal price = skuInfoMapper.get1010price(skuId);
        return price;
    }


}




