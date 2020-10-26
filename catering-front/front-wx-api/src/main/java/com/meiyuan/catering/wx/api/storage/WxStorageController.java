package com.meiyuan.catering.wx.api.storage;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.wx.service.common.WxContentCheckService;
import com.meiyuan.catering.wx.service.storage.WxStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName WxStorageController
 * @Description
 * @Author gz
 * @Date 2020/3/30 15:40
 * @Version 1.1
 */
@Api(tags = "文件上传相关")
@RestController
@RequestMapping(value = "wx/storage")
public class WxStorageController {
    @Autowired
    private WxStorageService storageService;

    @ApiOperation("单文件上传")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        return storageService.create(file);
    }

    @ApiOperation("批量上传文件")
    @PostMapping("/uploadBatch")
    public Result uploadBatch(@RequestParam("files") MultipartFile[] files) throws IOException {
        return storageService.createBatch(files);
    }

    @ApiOperation("阿里云OSS签名")
    @GetMapping("aliyunOssSignature")
    public Result aliyunOssSignature() {
        return storageService.aliyunOssSignature();
    }
}
