package com.meiyuan.catering.merchant.api.merchant;

import com.meiyuan.catering.allinpay.model.result.member.ApplyBindBankCardResult;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.dto.shop.bank.BankCardDTO;
import com.meiyuan.catering.merchant.dto.shop.bank.ShopNameAuthDTO;
import com.meiyuan.catering.merchant.service.merchant.MerchantShopBankService;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo;
import com.meiyuan.catering.pay.dto.member.ApplyBindBankCardDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author MeiTao
 * @Description 店铺结算信息完善
 * @Date  2020/3/22 0022 21:09
 */
@RestController
@RequestMapping("/app/bank")
@Api(tags = "店铺-店铺结算信息完善")
public class MerchantShopBankController {

    @Autowired
    MerchantShopBankService shopBankService;

    @PostMapping("/realNameAuth")
    @ApiOperation("1、门店实名认证 - 2、电子签约【1.5.0】")
    public Result<String> realNameAuth(@LoginMerchant MerchantTokenDTO tokenDTO,
                                                        @ApiParam("店铺id") @RequestBody ShopNameAuthDTO dto){
        dto.setShopId(tokenDTO.getShopId());
        return shopBankService.realNameAuth(dto);
    }

    @PostMapping("/getSignUrl")
    @ApiOperation("获取门店签约url【1.5.0】")
    public Result<String> getSignUrl(@LoginMerchant MerchantTokenDTO tokenDto){

        return shopBankService.getSignUrl(tokenDto.getShopId());
    }

    @GetMapping("/getNameAuthSmsCode/{phone}")
    @ApiOperation("实名认证获取短信验证码【1.5.0】")
    public Result getNameAuthSmsCode(@LoginMerchant MerchantTokenDTO tokenDto,
                                          @ApiParam("手机号") @PathVariable String phone){
        return shopBankService.getNameAuthSmsCode(tokenDto.getShopId(),phone);
    }

    @PostMapping("/getSmsCode")
    @ApiOperation("绑定银行卡获取短信验证码【1.5.0】")
    public Result<BankCardDTO> getSmsCode(@LoginMerchant MerchantTokenDTO tokenDto,
                                          @Valid @ApiParam("店铺id") @RequestBody ApplyBindBankCardDTO dto){
        dto.setBizUserId(String.valueOf(tokenDto.getShopId()));
        return shopBankService.getSmsCode(dto);
    }

    @PostMapping("/bindBankCard")
    @ApiOperation("绑定银行卡 【1.5.0】")
    public Result<ApplyBindBankCardResult> bindBankCard(@LoginMerchant MerchantTokenDTO tokenDto,
                                                      @Valid @ApiParam("店铺id") @RequestBody BankCardDTO dto){
        dto.setBizUserId(String.valueOf(tokenDto.getShopId()));
        return shopBankService.bindBankCard(dto);
    }

    @PostMapping("/getShopBankInfo")
    @ApiOperation("门店结算信息获取【1.5.0】")
    public Result<ShopBankInfoVo> getShopBankInfo(@LoginMerchant MerchantTokenDTO tokenDto){

        return shopBankService.getShopBankInfo(tokenDto.getShopId());
    }


}
