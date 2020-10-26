package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 11:57
 * @since v1.1.0
 */
@Data
public class UnbindPhoneResult extends AllinPayBaseResponseResult {

    private String phone;
    /**
     * 成功：OK 失败：error
     */
    private String result;
}
