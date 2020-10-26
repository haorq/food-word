package com.meiyuan.catering.admin.web.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.merchant.AdminMerchantService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.merchant.MerchantDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantDetailDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantQueryPage;
import com.meiyuan.catering.merchant.dto.merchant.MerchantVerifyDTO;
import com.meiyuan.catering.merchant.vo.merchant.MerchantPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 商户管理-商户管理
 * @Date  2020/3/12 0012 15:29
 */
@RestController
@RequestMapping("/admin/merchant/merchant")
@Api(tags = "商户管理-商户管理")
public class AdminMerchantController {
    @Resource
    AdminMerchantService adminMerchantService;

    @PostMapping("/merchantPage")
    @ApiOperation("管理平台商户列表分页查询【1.3.0】")
    public Result<IPage<MerchantPageVo>> merchantPage(@ApiParam("商户分页查询条件") @RequestBody MerchantQueryPage dto){
        return  adminMerchantService.merchantPage(dto);
    }

    @PostMapping("/saveOrUpdateMerchant")
    @ApiOperation("添加修改商户【1.2.0】")
    @LogOperation(value = "添加修改商户")
    public Result saveOrUpdateMerchant(@ApiParam("商户DTO") @RequestBody MerchantDTO dto){
        return  adminMerchantService.saveOrUpdateMerchant(dto);
    }

    @PostMapping("/queryMerchantDetail")
    @ApiOperation("商户详情获取【1.2.0】")
    public Result<MerchantDetailDTO> queryMerchantDetail(@RequestBody MerchantQueryPage dto){
        return  adminMerchantService.queryMerchantDetail(dto);
    }

    @PostMapping("/updateMerchantStatus")
    @ApiOperation(value = "商户状态修改 : 必传商户id，商户启用/禁用状态【1.2.0】")
    public Result<List<Long>> updateMerchantStatus(@ApiParam("商户DTO") @RequestBody MerchantDTO dto){
        return  adminMerchantService.updateMerchantStatus(dto);
    }

    @PostMapping("/verifyMerchantInfoUnique")
    @ApiOperation(value = "商户信息验重【1.2.0】")
    public Result<Boolean> verifyMerchantInfoUnique(@ApiParam("商户信息验重接收参数DTO") @RequestBody MerchantVerifyDTO dto){
        return adminMerchantService.verifyMerchantInfoUnique(dto);
    }

    @PostMapping("/handelMerchantData")
    @ApiOperation(value = "商户登录账号老数据处理【1.3.0】")
    public Result<Boolean> handelMerchantData(){
        return adminMerchantService.handelMerchantData();
    }

}
