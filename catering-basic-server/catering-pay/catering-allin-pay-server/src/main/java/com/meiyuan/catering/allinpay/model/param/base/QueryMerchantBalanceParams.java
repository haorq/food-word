package com.meiyuan.catering.allinpay.model.param.base;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/10/22 14:52
 * @since v1.5.0
 */
@Data
@Builder
public class QueryMerchantBalanceParams extends AllinPayBaseServiceParams {

    /**
     * 平台标准账户集编号
     */
    private String accountSetNo;
}
