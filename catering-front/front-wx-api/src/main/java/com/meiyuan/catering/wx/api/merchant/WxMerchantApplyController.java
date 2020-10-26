package com.meiyuan.catering.wx.api.merchant;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyDTO;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.user.SendPhoneCodeDTO;
import com.meiyuan.catering.wx.service.merchant.WxMerchantApplyService;
import com.meiyuan.catering.wx.service.storage.WxStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author herui
 * @description 商家入驻
 * @date 2020/09/26 12:36
 * @since v1.5.0
 */
@RestController
@RequestMapping("/api/merchantApply")
@Api(tags = "herui-商家入驻申请")
@Slf4j
public class WxMerchantApplyController {

    @Autowired
    private WxStorageService storageService;

    @Resource
    private WxMerchantApplyService wxMerchantApplyService;

    @ApiOperation("【商户申请】发送商户入驻申请验证码")
    @GetMapping("/sendCode")
    public Result<Boolean> sendCode(@RequestParam String phone) {
        return wxMerchantApplyService.sendCode(phone);
    }

    @ApiOperation("【商户申请】验证验证码")
    @PostMapping("/verifyCode")
    public Result<Boolean> verifyCode(@RequestBody SendPhoneCodeDTO dto) {
        return wxMerchantApplyService.verifyCode(dto);
    }

    @PostMapping("/insertShopApply")
    @ApiOperation("【商户申请】门店信息提交")
    public Result insertShopApply(@LoginUser UserTokenDTO token,@Validated @RequestBody ShopApplyDTO param){
        String shopName = param.getShopName().replaceAll("\\p{Cf}","");
        param.setShopName(shopName);
        if("".equals(shopName)){
            return Result.fail("店铺名称不能为空");
        }
        String contactName = param.getContactName().replaceAll("\\p{Cf}","");
        param.setContactName(contactName);
        if("".equals(contactName)){
            return Result.fail("联系人不能为空");
        }
        String doorNumber = param.getDoorNumber().replaceAll("\\p{Cf}","");
        param.setDoorNumber(doorNumber);
        if("".equals(doorNumber)){
            return Result.fail("详细地址不能为空");
        }
        param.setApplyUserId(token.getUserId());
        return this.wxMerchantApplyService.insertShopApply(param);
    }

    @ApiOperation("【商户申请】单文件上传")
    @PostMapping("/upload")
    public Result merchantApplyUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if(file.getSize() > 2*1024*1024){
            return Result.fail("文件大小最大不能超过2M");
        }
        return storageService.create(file);
    }

}
