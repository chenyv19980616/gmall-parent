package com.atguigu.gmall.model.vo.search;

import lombok.Data;

/**
 * @author chenyv
 * @create 2022-09-16 10:43
 * 封装所有检索条件
 */
@Data
public class SearchParamVo {
    Long category3Id;
    Long category2Id;
    Long category1Id;
    String keyword;
    String[] props;
    String trademark;
    String order = "1:desc";
    Integer pageNo = 1;
}
