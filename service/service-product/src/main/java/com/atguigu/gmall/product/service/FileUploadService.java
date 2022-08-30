package com.atguigu.gmall.product.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author chenyv
 * @create 2022-08-29 11:22
 */
public interface FileUploadService {
    /**
     * 文件上传
     *
     * @param file
     * @return 返回文件在Minio中的存储地址
     */
    String upload(MultipartFile file) throws Exception;

}
