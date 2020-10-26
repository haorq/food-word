package com.meiyuan.catering.allinpay.model.param.member;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 11:54
 * @since v1.1.0
 */
@Data
public class UnbindPhoneParams extends AllinPayBaseServiceParams {

    /**
     * 类型: String
     * 是否必填：是
     * 描述: 手机号
     * 示例值:
     */
    private String phone;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 验证码
     * 示例值:
     */
    private String verificationCode;


}
