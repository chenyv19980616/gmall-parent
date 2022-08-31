package com.atguigu.gmall.model.to;

import lombok.Data;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-08-30 16:57
 * <p>
 * DDD：领域驱动设计
 * 三级分类树形结构
 */
@Data
public class CategoryTreeTo {
    private Long categoryId;
    private String categoryName;
    private List<CategoryTreeTo> categoryChild;
}
