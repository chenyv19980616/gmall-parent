package com.atguigu.gmall.search.service;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.SearchParamVo;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;

/**
 * @author chenyv
 * @create 2022-09-15 15:14
 */
public interface GoodsService {
    void saveGoods(Goods goods);

    void deleteGoods(Long skuId);

    /**
     * 去es检索商品
     * @param paramVo
     * @return
     */
    SearchResponseVo search(SearchParamVo paramVo);

    void updateHotScore(Long skuId, Long score);
}
