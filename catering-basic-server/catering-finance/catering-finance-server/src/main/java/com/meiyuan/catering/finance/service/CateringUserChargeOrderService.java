package com.meiyuan.catering.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.pay.ChargeOrder;
import com.meiyuan.catering.finance.entity.CateringUserChargeOrderEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
public interface CateringUserChargeOrderService extends IService<CateringUserChargeOrderEntity> {

    /**
     * 描述:通过充值订单号查询充值订单
     *
     * @param rechargeNo
     * @return com.meiyuan.catering.finance.entity.CateringUserChargeOrderEntity
     * @author zengzhangni
     * @date 2020/3/27 16:41
     */
    ChargeOrder getByRechargeNo(String rechargeNo);


    /**
     * 描述:查询未支付的充值订单
     *
     * @param
     * @return java.util.List<com.meiyuan.catering.core.dto.pay.ChargeOrder>
     * @author zengzhangni
     * @date 2020/5/19 16:35
     * @since v1.1.0
     */
    List<ChargeOrder> autoOrderCancle();


    /**
     * 描述:通过id查询充值订单
     *
     * @param id
     * @return com.meiyuan.catering.core.dto.pay.ChargeOrder
     * @author zengzhangni
     * @date 2020/5/19 16:31
     * @since v1.1.0
     */
    ChargeOrder getOrderById(Long id);

    /**
     * 描述:充值成功更新充值订单
     *
     * @param id
     * @param receivedAmount
     * @param cashCoupon
     * @param transactionId
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/20 9:22
     * @since v1.1.0
     */
    Boolean updateChargeOrder(Long id, BigDecimal receivedAmount, BigDecimal cashCoupon, String transactionId);

    /**
     * 描述:添加充值订单
     *
     * @param userId
     * @param userType
     * @param ruleId
     * @param rechargeOrderNo
     * @param rechargeAccount
     * @param givenAccount
     * @param ip
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/20 9:27
     * @since v1.1.0
     */
    Boolean saveChargeOrder(Long userId, Integer userType, Long ruleId, String rechargeOrderNo, BigDecimal rechargeAccount, BigDecimal givenAccount, String ip);
}
