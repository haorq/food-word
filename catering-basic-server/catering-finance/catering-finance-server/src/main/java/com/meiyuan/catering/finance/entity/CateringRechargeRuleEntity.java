package com.meiyuan.catering.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 充值活动规则
 *
 * @author zengzhangni
 * @date 2020-03-16
 */
@Data
@TableName("catering_recharge_rule")
public class CateringRechargeRuleEntity extends IdEntity {


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
