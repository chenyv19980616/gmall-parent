package com.atgugiu.gmall.web.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-08-30 18:50
 */
@FeignClient("service-product")     //声明这是一个远程调用的客户端，调用service-product微服务功能
public interface CategoryFeignClient {
    /**
     * 1. 给service-product发送get方式的请求 路径是/api/inner/rpc/product/category/tree
     * 2. 拿到远程的响应json结果后转成Result类型的对象 并且返回的数据是Result<List<CategoryTreeTo>>
     *
     * @return
     */
    @GetMapping("/api/inner/rpc/product/category/tree")
    Result<List<CategoryTreeTo>> getAllCategorysWithTree();
}
