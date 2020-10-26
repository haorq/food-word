package com.meiyuan.catering.wx.service.merchant;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.merchant.WxMerchantIndexV13DTO;
import com.meiyuan.catering.wx.dto.merchant.v1.WxMerchantIndexV1RequestDTO;
import com.meiyuan.catering.wx.utils.support.MerchantIndexSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yaoozu
 * @description 商户服务
 * @date 2020/3/2414:08
 * @since v1.0.0
 */
@Service
@Slf4j
public class WxMerchantService {

    @Autowired
    private MerchantIndexSupport merchantIndexSupport;


    public Result<WxMerchantIndexV13DTO> index(UserTokenDTO token, WxMerchantIndexV1RequestDTO dto) {
        long millis = System.currentTimeMillis();

        WxMerchantIndexV13DTO wxMerchantIndexV13DTO = new WxMerchantIndexV13DTO();

        //设置公共信息
        merchantIndexSupport.setCommonInfo(wxMerchantIndexV13DTO, token, dto);
        //设置基础信息
        merchantIndexSupport.setBaseInfo(wxMerchantIndexV13DTO, token, dto);
        //设置配置信息
        merchantIndexSupport.setConfigInfo(wxMerchantIndexV13DTO, token, dto);
        //设置分类信息(分类,商品)
        merchantIndexSupport.setCategoryInfo(wxMerchantIndexV13DTO, token, dto);
        //设置优惠活动信息
        merchantIndexSupport.setDiscountsInfo(wxMerchantIndexV13DTO, token, dto);

        log.debug("商户首页请求时间:{}ms", System.currentTimeMillis() - millis);
        return Result.succ(wxMerchantIndexV13DTO);
    }
}
