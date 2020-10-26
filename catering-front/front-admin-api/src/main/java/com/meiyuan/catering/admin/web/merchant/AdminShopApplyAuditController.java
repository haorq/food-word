package com.meiyuan.catering.admin.web.merchant;

import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.service.admin.AdminStorageService;
import com.meiyuan.catering.admin.service.merchant.AdminShopApplyService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyListDTO;

import com.meiyuan.catering.merchant.vo.ShopApplyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/admin/merchant/audit")
@Api(tags = "V1.5.0-商户入驻申请审核")
public class AdminShopApplyAuditController {

    @Autowired
    private AdminShopApplyService adminShopApplyService;

    @Autowired
    private AdminStorageService storageService;

    @PostMapping("/listShop")
    @ApiOperation("【入驻商家申请】店铺查询所有")
    public Result<PageData<ShopApplyVO>>listShopApply(@RequestBody ShopApplyListDTO dto ) {
        return adminShopApplyService.listShopApply(dto);
    }

    @GetMapping("/detail/{applyId}")
    @ApiOperation("【入驻商家申请】店铺申请查看详情")
    public Result<ShopApplyVO>detail(@PathVariable String applyId) {
        return adminShopApplyService.shopApplyDetail(applyId);
    }

    @PostMapping("/submit")
    @ApiOperation("【入驻商家申请】确认(店铺申请和修改的审核信息)审核结果")
    public Result ShopApplyConfirm(@RequestAttribute("info") CateringAdmin admin,@RequestBody @Validated ShopApplyDTO dto ) {
        String shopName = dto.getShopName().replaceAll("\\p{Cf}","");
        dto.setShopName(shopName);
        if("".equals(shopName)){
            return Result.fail("店铺名称不能为空");
        }
        String contactName = dto.getContactName().replaceAll("\\p{Cf}","");
        dto.setContactName(contactName);
        if("".equals(contactName)){
            return Result.fail("联系人不能为空");
        }
        String doorNumber = dto.getDoorNumber().replaceAll("\\p{Cf}","");
        dto.setDoorNumber(doorNumber);
        if("".equals(doorNumber)){
            return Result.fail("详细地址不能为空");
        }
        dto.setUpdateBy(admin.getId());
        return adminShopApplyService.ShopApplyConfirm(dto);
    }

    @ApiOperation("【入驻商家申请】单文件上传")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        if(file.getSize() < 1024*1024*2){
            return Result.fail("文件大小不超过2M");
        }
        return storageService.create(file);
    }

}
