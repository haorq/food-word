package com.meiyuan.catering.core.util.dada.client;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 发单到达达成功后，达达返回数据
 *
 * @author lh
 */
@Data
public class DadaRetAfterAddOrder {
    /**
     * 配送距离(单位：米)
     */
    private BigDecimal distance;
    /**
     * 实际运费(单位：元)，运费减去优惠券费用
     */
    private BigDecimal fee;
    /**
     * 运费(单位：元)
     */
    private BigDecimal deliveryFee;
    /**
     * 优惠券费用(单位：元)
     */
    private BigDecimal couponFee;
    /**
     * 小费(单位：元)
     */
    private BigDecimal tips;
    /**
     * 保价费(单位：元)
     */
    private BigDecimal insuranceFee;

}
