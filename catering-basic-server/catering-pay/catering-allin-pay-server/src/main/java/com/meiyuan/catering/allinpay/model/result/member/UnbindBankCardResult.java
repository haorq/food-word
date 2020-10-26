package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 11:50
 * @since v1.1.0
 */
@Data
public class UnbindBankCardResult extends AllinPayBaseResponseResult {
    /**
     * 银行卡号 	AES加密
     */
    private String cardNo;
}
