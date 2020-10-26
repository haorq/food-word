package com.meiyuan.catering.merchant.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.bank.BankCardDTO;
import com.meiyuan.catering.merchant.dto.shop.bank.ShopBankDTO;
import com.meiyuan.catering.merchant.dto.shop.bank.ShopNameAuthDTO;
import com.meiyuan.catering.merchant.service.CateringShopBankService;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
  * @Author MeiTao
  * @Date 2020/5/19 0019 9:31
  * @Description 简单描述:商户client
  * @Since version-1.0
  */
@Service
public class ShopBankClient {

     @Autowired
     CateringShopBankService shopBankService;

    /**
     * 方法描述 : app-门店结算信息实名认证
     * @Author: MeiTao
     * @Date: 2020/9/30 0030 9:39
     * @Since version-1.5.0
     */
    public Result<String> addShopAuthInfo(ShopNameAuthDTO dto) {
        return shopBankService.addShopAuthInfo(dto);
    }


    public void saveBankInfo(ShopBankDTO shopBankDto) {
        shopBankService.saveBankInfo(shopBankDto);
    }

    public void bindBankCard(BankCardDTO dto) {
        shopBankService.bindBankCard(dto);
    }

    public Result<ShopBankInfoVo> getShopBankInfo(Long shopId) {
        return shopBankService.getShopBankInfo(shopId);
    }

    public void bindPhone(Long shopId, String phone) {
        shopBankService.bindPhone(shopId,phone);
    }
}
