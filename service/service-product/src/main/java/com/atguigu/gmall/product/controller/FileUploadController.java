package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chenyv
 * @create 2022-08-27 17:49
 * 文件上传
 */
@RequestMapping("/admin/product")
@RestController
public class FileUploadController {


    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file") MultipartFile file) {


        return Result.ok();
    }
}
