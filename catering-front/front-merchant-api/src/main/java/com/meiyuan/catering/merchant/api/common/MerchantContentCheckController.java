package com.meiyuan.catering.merchant.api.common;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.service.common.MerchantContentCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author MeiTao
 * @Description 文本/图片 类容审查
 * @Date  2020/3/28 0028 9:36
 */
@RestController
@RequestMapping(value = "app/check")
@Api(tags = "文本/图片 内容审查")
public class MerchantContentCheckController {
    @Autowired
    private MerchantContentCheckService contentCheckService;


    @ApiOperation("图片审查 -- 图片")
    @PostMapping("/checkImageByFile")
    public Result checkImageByFile(@RequestParam("file") MultipartFile file){
        return contentCheckService.checkImageByFile(file);
    }


    @ApiOperation("图片审查 -- 图片地址")
    @GetMapping("/checkImageByUrl")
    public Result checkImageByUrl(@ApiParam("图片地址") @RequestParam("url") String url){
        return contentCheckService.checkImageByUrl(url);
    }

    @ApiOperation("文本内容审核")
    @GetMapping("/checkText")
    public Result checkText(@ApiParam("待审核文本内容") @RequestParam("text") String text){
        return contentCheckService.checkText(text);
    }


}
