package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/24 17:10
 * @since v1.1.0
 */
@Data
public class SendVerificationCodeResult extends AllinPayBaseResponseResult {
    /**
     * phone
     */
    private String phone;
}
