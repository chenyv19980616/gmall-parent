package com.atguigu.gmall.product;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

/**
 * @author chenyv
 * @create 2022-08-29 10:42
 */
//@SpringBootTest       //可以测试SpringBoot的所有组件功能，启动慢
public class MinioTest {

    @Test
    public void uploadFile() throws Exception {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient =
                    new MinioClient(
                            "http://192.168.6.200:9000",
                            "admin",
                            "admin123456");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("gmall");
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 如果桶不存在需要先创建一个桶。
                minioClient.makeBucket("gmall");
            }

            // 使用putObject上传一个文件到存储桶中。
            /**
             * String bucketName:桶名
             * String objectName:对象名
             * InputStream stream:文件流
             * PutObjectOptions options:上传的参数设置
             */
            FileInputStream fileInputStream = new FileInputStream("E:\\尚硅谷Java全程\\stage09-雷丰阳\\资料\\03 商品图片\\品牌\\oppo.png");
            PutObjectOptions options = new PutObjectOptions(fileInputStream.available(), -1L);
            options.setContentType("image/png");
            minioClient.putObject(
                    "gmall",
                    "oppo.png",
                    fileInputStream,
                    options
            );
            System.out.println("上传成功！");
        } catch (MinioException e) {
            System.out.println("发生错误: " + e);
        }
    }
}