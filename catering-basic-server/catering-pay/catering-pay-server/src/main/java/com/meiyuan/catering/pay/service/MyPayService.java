package com.meiyuan.catering.pay.service;

import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.pay.dto.BasePayDto;
import com.meiyuan.catering.pay.dto.OrderQueryResult;
import org.springframework.scheduling.annotation.Async;

/**
 * @author zengzhangni
 * @date 2020/4/2
 */
public interface MyPayService {


    /**
     * 描述: 统一支付
     *
     * @param dto
     * @return java.lang.Object
     * @author zengzhangni
     * @date 2020/4/2 15:22
     */
    Object pay(BasePayDto dto);


    /**
     * 描述: 取消订单 退款
     *
     * @param order
     * @return void
     * @author zengzhangni
     * @date 2020/4/2 14:55
     */
    void cancel(Order order);

    /**
     * 描述: 异步取消
     *
     * @param order
     * @return void
     * @author zengzhangni
     * @date 2020/4/2 16:24
     */
    @Async
    void asyncCancel(Order order);

    /**
     * 描述: 商家同意用户申请的退款单 退款
     *
     * @param refundOrder
     * @return void
     * @author zengzhangni
     * @date 2020/4/2 14:56
     */
    void refund(RefundOrder refundOrder);

    /**
     * 描述:关闭订单
     *
     * @param order
     * @return void
     * @author zengzhangni
     * @date 2020/4/11 10:50
     */
    void closeOrder(Order order);

    /**
     * 描述:查询订单
     *
     * @param tradingFlow
     * @return com.meiyuan.catering.pay.dto.OrderQueryResult
     * @author zengzhangni
     * @date 2020/4/11 14:48
     */
    OrderQueryResult queryOrder(String tradingFlow);

}
