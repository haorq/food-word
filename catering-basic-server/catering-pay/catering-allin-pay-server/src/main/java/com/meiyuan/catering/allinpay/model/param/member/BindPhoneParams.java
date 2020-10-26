package com.meiyuan.catering.allinpay.model.param.member;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/24 18:14
 * @since v1.1.0
 */
@Data
@Builder
public class BindPhoneParams extends AllinPayBaseServiceParams {
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 手机号
     * 示例值: xxx
     */
    private String phone;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 验证码
     * 示例值: xxx
     */
    private String verificationCode;
}
