package com.meiyuan.catering.allinpay.model.result.base;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/10/22 14:55
 * @since v1.5.0
 */
@Data
public class QueryMerchantBalanceResult extends AllinPayBaseResponseResult {

    private Long allAmount;
    private Long freezeAmount;
}
