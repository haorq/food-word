package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/24 18:15
 * @since v1.1.0
 */
@Data
public class BindPhoneResult extends AllinPayBaseResponseResult {

    /**
     * phone
     */
    private String phone;
}
