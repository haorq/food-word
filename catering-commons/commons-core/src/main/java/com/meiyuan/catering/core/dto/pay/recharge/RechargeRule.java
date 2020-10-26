package com.meiyuan.catering.core.dto.pay.recharge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/5/20 9:31
 * @since v1.1.0
 */
@Data
public class RechargeRule extends IdEntity {
    /**
     * 充值活动id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long activityId;
    /**
     * 充值金额
     */
    private BigDecimal rechargeAccount;
    /**
     * 优惠折扣(98%)
     */
    private Byte discountRate;
    /**
     * 赠送金额
     */
    private BigDecimal givenAccount;
}
