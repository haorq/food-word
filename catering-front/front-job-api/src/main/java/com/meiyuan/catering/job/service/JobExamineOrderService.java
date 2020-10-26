package com.meiyuan.catering.job.service;

import com.meiyuan.catering.allinpay.model.result.order.AllinPayOrderDetailResult;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.util.CacheLockUtil;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.pay.util.PayLock;
import com.meiyuan.catering.pay.util.PaySupport;
import com.meiyuan.catering.pay.util.PayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/4/13
 **/
@Slf4j
@Service
public class JobExamineOrderService {

    @Resource
    private OrderClient orderClient;
    @Resource
    private PaySupport support;
    @Resource
    private PayLock lock;


    /**
     * 描述:  检查待支付
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/4/13 14:18
     */
    public void examineOrder() {
        List<Order> list = orderClient.unPaidOrderList();

        list.forEach(order -> {
            try {
                String orderNumber = order.getOrderNumber();
                AllinPayOrderDetailResult result = PayUtil.allinQueryOrder(order.getTradingFlow());
                if (PayUtil.isHaveWxPay(result)) {
                    //和支付回调用同一把锁
                    lock.payLock(CacheLockUtil.orderPayNotifyLock(orderNumber), () -> {
                        //已微信支付 主动处理
                        support.wxPayOrderDispose(result);
                    });
                }
            } catch (Exception e) {
                log.error("检查待支付订单 异常:{}", e.getMessage());
            }
        });
    }
}
