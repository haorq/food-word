package com.meiyuan.catering.merchant.pc.api.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantLimitQueryDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantShopAddDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantShopUpdateDTO;
import com.meiyuan.catering.merchant.dto.shop.RegisterPhoneChangeDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopQueryDTO;
import com.meiyuan.catering.merchant.pc.service.merchant.MerchantPcMerchantService;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPageVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author MeiTao
 * @Description 商户pc端-商户管理
 * @Date  2020/3/12 0012 15:29
 */
@RestController
@RequestMapping("/merchant")
@Api(tags = "商户管理-商户管理")
public class MerchantPcMerchantController {
    @Resource
    MerchantPcMerchantService merchantService;

    @PostMapping("/shopPage")
    @ApiOperation("店铺列表")
    public Result<IPage<CateringMerchantPageVo>> shopPage(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                          @RequestBody MerchantLimitQueryDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return merchantService.shopPage(dto);
    }

    @PostMapping("/insertShop")
    @ApiOperation("添加店铺、自提点【1.2.0】")
    public Result insertShop(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                             @ApiParam("商户店铺信息") @RequestBody MerchantShopAddDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return merchantService.insertShop(dto);
    }

    @PostMapping("/updateShop")
    @ApiOperation("修改店铺信息【1.2.0】")
    public Result updateShop(@ApiParam("商户店铺修改信息") @RequestBody MerchantShopUpdateDTO dto){
        return merchantService.updateMerchantShop(dto);
    }

    @PostMapping("/updateShopRegisterPhone")
    @ApiOperation("pc - 店铺注册手机号修改")
    public Result<String> updateShopRegisterPhone(@ApiParam("注册手机号修改DTO") @RequestBody RegisterPhoneChangeDTO dto){
        return merchantService.updateShopRegisterPhone(dto);
    }

    @GetMapping("/getMerchantShopDetail/{shopId}")
    @ApiOperation("商户店铺详情")
    public Result<MerchantShopDetailVo> getMerchantShopDetail(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@ApiParam("shopId") @PathVariable Long shopId){
        ShopQueryDTO dto = new ShopQueryDTO();
        dto.setId(shopId);
        dto.setMerchantId(token.getAccountTypeId());
        return merchantService.getMerchantShopDetail(dto);
    }

    @GetMapping("/getShopService")
    @ApiOperation("获取门店服务类型")
    public Result<String> getShopService(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO dto){

        return merchantService.getShopService(dto.getAccountTypeId());
    }

}
