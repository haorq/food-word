package com.meiyuan.catering.allinpay.model.param.order;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/28 16:30
 * @since v1.1.0
 */
@Data
@Builder
public class ApplicationTransferParams extends AllinPayBaseServiceParams {
    /**
     * 商户系统转账订单号，商户系统唯一
     */
    private String bizTransferNo;
    /**
     * 是 	- 	源账户集编号 	通商云统一的标准账户集合的编号。
     */
    private String sourceAccountSetNo;
    /**
     * 是 	- 	目标商户系统用户标识，商户系统中唯一编号。 	收款会员的BizUserId
     */
    private String targetBizUserId;
    /**
     * 是 	- 	目标账户集编号
     */
    private String targetAccountSetNo;
    /**
     * 是 	- 	金额
     */
    private Long amount;
    /**
     * 否 	- 	扩展信息
     */
    private String extendInfo;


}
