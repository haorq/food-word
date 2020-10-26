package com.meiyuan.catering.allinpay.model.result.order;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/28 16:32
 * @since v1.1.0
 */
@Data
public class ApplicationTransferResult extends AllinPayBaseResponseResult {
    /**
     * 是 	- 	通商云转账订单号
     */
    private String transferNo;
    /**
     * 是 	- 	商户系统转账订单号
     */
    private String bizTransferNo;
    /**
     * 是 	- 	金额
     */
    private Long amount;
    /**
     * 否 	- 	扩展信息
     */
    private String extendInfo;
}
