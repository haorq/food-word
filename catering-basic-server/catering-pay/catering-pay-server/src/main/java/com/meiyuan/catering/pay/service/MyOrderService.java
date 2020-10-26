package com.meiyuan.catering.pay.service;

import com.meiyuan.catering.allinpay.model.result.order.AllinPayBatchAgentPayResult;
import com.meiyuan.catering.allinpay.model.result.order.AllinPaySignalAgentPayResult;
import com.meiyuan.catering.allinpay.model.result.order.AllinpayQueryBalanceResult;

import java.util.Set;

/**
 * 描述: 订单服务
 *
 * @author zengzhangni
 * @date 2020/10/9 9:24
 * @since v1.5.0
 */
public interface MyOrderService {


    /**
     * 描述: 单笔托管代付（标准版）
     *
     * @param orderId 订单ID
     * @return com.meiyuan.catering.allinpay.model.result.order.AllinPaySignalAgentPayResult
     * @author zengzhangni
     * @date 2020/10/9 9:49
     * @since v1.5.0
     */
    AllinPaySignalAgentPayResult signalAgentPay(Long orderId);

    /**
     * 描述:批量托管代付（标准版）
     *
     * @param orderId 订单ID
     * @return com.meiyuan.catering.allinpay.model.result.order.AllinPayBatchAgentPayResult
     * @author zengzhangni
     * @date 2020/10/9 9:26
     * @since v1.5.0
     */
    AllinPayBatchAgentPayResult batchAgentPay(Long orderId);

    /**
     * 描述：平台转账
     * @param orderId 订单ID
     * @author: GongJunZheng
     * @date: 2020/10/9 11:56
     * @return: void
     * @version V1.5.0
     **/
    void applicationTransfer(Long orderId);

    /**
    * 描述：提现申请
    * @param shopId 门店ID
    * @author: GongJunZheng
    * @date: 2020/10/9 11:23
    * @return: void
    * @version V1.5.0
    **/
    void withdrawApply(Long shopId);

    /**
     * 描述：提现申请
     * @param shopIdList 门店ID集合
     * @author: GongJunZheng
     * @date: 2020/10/9 11:23
     * @return: void
     * @version V1.5.0
     **/
    void withdrawApply(Set<Long> shopIdList);

    /**
    * 处理异常订单分账信息
    * @author: GongJunZheng
    * @date: 2020/10/10 13:43
    * @return: void
    * @version V1.5.0
    **/
    void makeOrderAbnormalSplitBill();

    /**
    * 处理异常补贴分账信息
    * @author: GongJunZheng
    * @date: 2020/10/10 15:10
    * @return: void
    * @version V1.5.0
    **/
    void makeSubsidyAbnormalSplitBill();

    /**
     * 描述:查询余额
     *
     * @param shopId
     * @return com.meiyuan.catering.allinpay.model.result.order.AllinpayQueryBalanceResult
     * @author zengzhangni
     * @date 2020/10/12 16:12
     * @since v1.5.0
     */
    AllinpayQueryBalanceResult queryBalance(Long shopId);
}
