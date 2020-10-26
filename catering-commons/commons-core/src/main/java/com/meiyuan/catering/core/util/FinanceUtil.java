package com.meiyuan.catering.core.util;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.generator.CodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zengzhangni
 * @date 2020/3/28
 */
@Slf4j
public class FinanceUtil {

    /**
     * 优惠状态，0--无，1--现金优惠/现金劵，2--折扣，3--现金折扣
     */
    public final static Integer NOT = 0;
    public final static Integer CASH_DISCOUNTS = 1;
    public final static Integer DISCOUNT = 2;
    public final static Integer CASH_DISCOUNT = 3;

    /**
     * 描述:余额支付流水号
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/28 11:40
     */
    public static String balanceOrderTransactionNo() {
        return CodeGenerator.balanceOrderTransactionNo();
    }

    /**
     * 描述: 余额退款 退款单号
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/28 11:40
     */
    public static String balanceRefundTransactionNo() {
        return CodeGenerator.balanceRefundTransactionNo();
    }

    /**
     * 描述:余额充值 充值单号
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/28 11:40
     */
    public static String rechargeNo() {
        return CodeGenerator.rechargeOrderNo();
    }

    /**
     * 描述:微信支付系统流水号
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/5/21 9:08
     * @since v1.1.0
     */
    public static String wxPayNo(String shopCode) {
        if (StringUtils.isBlank(shopCode)) {
            log.error("门店编号为空");
            throw new CustomException("门店编号为空");
        }
        return CodeGenerator.wxPayNo(shopCode);
    }

    /**
     * 描述:系统交易流水编号
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/31 14:00
     */
    public static String systemTransactionNo() {
        return CodeGenerator.systemTransactionNo();
    }

    /**
     * 描述:退款单号
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/28 11:40
     */
    public static String refundNo(String shopCode) {
        if (StringUtils.isBlank(shopCode)) {
            log.error("门店编号为空");
            throw new CustomException("门店编号为空");
        }
        return CodeGenerator.refundOrderNo(shopCode);
    }

    /**
     * 描述:充值订单退款单号
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/4/13
     */
    public static String refundChargeOrderNo() {
        return CodeGenerator.refundChargeOrderNo();
    }

    /**
     * 描述: 是否有现金优惠
     *
     * @param
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/3/30 14:49
     */
    public static Integer isCashDiscounts(Integer discounts) {
        return discounts > 0 ? CASH_DISCOUNTS : NOT;
    }

}
