package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chenyv
 * @create 2022-08-27 17:49
 * 文件上传
 */
@Api(tags = "文件上传控制器")
@RequestMapping("/admin/product")
@RestController
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

    @ApiOperation("文件上传")
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file") MultipartFile file) throws Exception {
        String url = fileUploadService.upload(file);
        return Result.ok(url);
    }
}
