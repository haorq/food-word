package com.meiyuan.catering.merchant.service;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.bank.BankCardDTO;
import com.meiyuan.catering.merchant.dto.shop.bank.ShopBankDTO;
import com.meiyuan.catering.merchant.dto.shop.bank.ShopNameAuthDTO;
import com.meiyuan.catering.merchant.entity.CateringShopBankEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo;

/**
 * @Author MeiTao
 * @Date 2020/9/30 0030 9:41
 * @Description 简单描述 :  门店结算信息
 * @Since version-1.5.0
 */
public interface CateringShopBankService extends IService<CateringShopBankEntity> {

    /**
     * 方法描述 :  app-门店结算信息实名认证
     * @Author: MeiTao
     * @Date: 2020/9/30 0030 9:41
     * @param dto 请求参数
     * @return: com.meiyuan.catering.core.util.Result<java.lang.String>
     * @Since version-1.5.0
     */
    Result<String> addShopAuthInfo(ShopNameAuthDTO dto);

    /**
     * 方法描述 : 保存用户绑定银行卡信息
     * @Author: MeiTao
     * @Date: 2020/9/30 0030 14:49
     * @param shopBankDto 请求参数
     * @return: void
     * @Since version-1.5.0
     */
    void saveBankInfo(ShopBankDTO shopBankDto);

    /**
     * 方法描述 : 获取门店与通联绑定相关信息
     * @Author: MeiTao
     * @Date: 2020/9/30 0030 10:41
     * @param shopId 请求参数
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo>
     * @Since version-1.5.0
     */
    Result<ShopBankInfoVo> getShopBankInfo(Long shopId);

    /**
     * 方法描述 : 完成银行卡绑定
     * @Author: MeiTao
     * @Date: 2020/9/30 0030 15:08
     * @param dto 请求参数
     * @return: void
     * @Since version-1.5.0
     */
    void bindBankCard(BankCardDTO dto);

    /**
     * 方法描述 : tl实名认证手机号绑定
     * @Author: MeiTao
     * @Date: 2020/10/10 0010 19:54
     * @param shopId
     * @param phone 请求参数
     * @return: void
     * @Since version-1.5.0
     */
    void bindPhone(Long shopId, String phone);
}
