package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.product.config.minio.MinioProperties;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @author chenyv
 * @create 2022-08-29 11:23
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) throws Exception {
        // 1. 创建Minio客户端
        // 2.判断这个桶是否存在
        boolean exists = minioClient.bucketExists(minioProperties.getBucketName());
        if (!exists) {
            //如果不存在则创建这个桶
            minioClient.makeBucket(minioProperties.getBucketName());
        }
        // 3.给桶里面上传文件
        String date = DateUtil.formatDate(new Date());
        String filename = UUID.randomUUID().toString().replace("-", "")
                + "_" + file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        PutObjectOptions options = new PutObjectOptions(inputStream.available(), -1L);
        //默认是二进制，必须修改成对应的图片类型
        options.setContentType("image/png");
        // 4.文件上传
        minioClient.putObject(minioProperties.getBucketName(), date + "/" + filename, inputStream, options);
        // 5.返回刚才上传文件的可访问路径
        return minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + date + "/" + filename;
    }
}
