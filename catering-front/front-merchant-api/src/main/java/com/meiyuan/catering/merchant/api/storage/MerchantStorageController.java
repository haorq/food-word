package com.meiyuan.catering.merchant.api.storage;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.service.common.MerchantContentCheckService;
import com.meiyuan.catering.merchant.service.storage.MerchantStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName MerchantStorageController
 * @Description 文件存储Controller
 * @Author gz
 * @Date 2020/3/18 15:14
 * @Version 1.1
 */
@RestController
@RequestMapping(value = "merchant/storage")
@Api(tags = "文件上传相关")
public class MerchantStorageController {
    @Autowired
    private MerchantStorageService storageService;

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


}
