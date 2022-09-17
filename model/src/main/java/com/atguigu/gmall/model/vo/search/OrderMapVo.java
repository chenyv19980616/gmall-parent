package com.atguigu.gmall.model.vo.search;

import lombok.Data;

/**
 * @author chenyv
 * @create 2022-09-16 11:51
 */
@Data
public class OrderMapVo {
    private String type;    //排序类型：1 综合 2 价格
    private String sort;    //排序规则
}
