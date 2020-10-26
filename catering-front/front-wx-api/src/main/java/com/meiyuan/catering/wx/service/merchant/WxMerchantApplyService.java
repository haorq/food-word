package com.meiyuan.catering.wx.service.merchant;

import com.meiyuan.catering.core.config.SmsProperties;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.notify.NotifyRestrictService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.notify.RestrictConstant;
import com.meiyuan.catering.core.notify.RestrictNumEnum;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyDTO;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.user.SendPhoneCodeDTO;
import com.meiyuan.catering.wx.utils.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Objects;


@Service
@Slf4j
public class WxMerchantApplyService {


    @Resource
    private SmsProperties smsProperties;

    @Autowired
    private NotifyRestrictService restrictService;

    @Resource
    private WechatUtils wechatUtils;

    @Resource
    MerchantClient merchantClient;

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    public Result<Boolean> sendCode(String phone) {
        String code;
        if (Boolean.TRUE.equals(smsProperties.getCode())) {
            code = CodeGenerator.randomCode(6);
            //发送验证码
            restrictService.notifySmsTemplate(phone, NotifyType.VERIFY_CODE_NOTIFY, RestrictNumEnum.SHOP_APPLY, RestrictConstant.WX_SHOP_APPLY, code);
        } else {
            code = smsProperties.getDefaultCode();
        }
        log.debug("发送商户入驻申请手机验证码:phone={},code={}", phone, code);
        //保存验证码
        wechatUtils.saveSmsAuthCode(phone, code);
        return Result.succ();
    }

    /**
     * 验证验证码
     *
     * @param dto
     * @return
     */
    public Result<Boolean> verifyCode(SendPhoneCodeDTO dto) {
        String phone = dto.getPhone();
        String authCode = wechatUtils.getSmsAuthCode(phone);
        if (StringUtils.isEmpty(authCode)) {
            return Result.fail("验证码已失效");
        }
        boolean equals = Objects.equals(dto.getCode(), authCode);
        if (equals) {
            //验证成功 删除验证码
            wechatUtils.removeSmsAuthCode(phone);
        }
        return equals ? Result.succ() : Result.fail("验证码错误，请重新输入");
    }


    public Result insertShopApply(ShopApplyDTO shopApplyDTO){
        return merchantClient.insertShopApply(shopApplyDTO);
    }
}
