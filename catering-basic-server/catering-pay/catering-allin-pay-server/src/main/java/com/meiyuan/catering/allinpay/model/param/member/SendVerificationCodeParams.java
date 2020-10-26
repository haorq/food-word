package com.meiyuan.catering.allinpay.model.param.member;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendVerificationCodeParams extends AllinPayBaseServiceParams {
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 手机号
     * 示例值: xxx
     */
    private String phone;
    /**
     * 类型: Long
     * 是否必填：是
     * 描述: 验证码类型
     * 示例值: 9-绑定手机 6-解绑手机
     */
    private Long verificationCodeType;

}
