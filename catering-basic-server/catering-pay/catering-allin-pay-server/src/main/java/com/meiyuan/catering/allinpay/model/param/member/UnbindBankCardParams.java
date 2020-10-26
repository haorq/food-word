package com.meiyuan.catering.allinpay.model.param.member;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 11:49
 * @since v1.1.0
 */
@Data
public class UnbindBankCardParams extends AllinPayBaseServiceParams {
    /**
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述: 银行卡号
     * 示例值:
     */
    private String cardNo;
}
