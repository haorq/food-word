package com.meiyuan.catering.job.service.finance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lhm
 * @date 2020/4/7 14:52
 **/
@Slf4j
@Service
public class JobChargeOrderService {

    /**
     * 充值订单过期自动取消
     */
    @Transactional(rollbackFor = Exception.class)
    public void autoOrderCancle() {
        try {
            log.info("无待关闭的充值订单");
//            List<ChargeOrder> entities = chargeOrderClient.autoOrderCancle().getData();
//            //微信关闭充值订单
//            closeChargeOrder(entities);
        } catch (Exception e) {
            log.error("充值订单过期自动取消 异常:{}", e.getMessage());
        }
    }

//    /**
//     * 描述:如果待支付的充值订单 有微信支付,关闭时退款
//     *
//     * @param order
//     * @return void
//     * @author zengzhangni
//     * @date 2020/4/13 11:34
//     */
//    private void closeChargeOrder(List<ChargeOrder> order) {
//        order.forEach(entity -> {
//            try {
//                //和充值支付回调用同一把锁
//                lock.payLock(CacheLockUtil.rechargePayNotifyLock(entity.getRechargeNo()), () -> {
//                    ChargeOrder chargeOrder = ClientUtil.getDate(chargeOrderClient.getOrderById(entity.getId()));
//                    if (Objects.equals(chargeOrder.getStatus(), OrderStatusEnum.UNPAID.getValue())) {
//                        myWxPayService.closeChargeOrder(chargeOrder);
//                    } else {
//                        log.debug("{}:充值订单状态不是待支付,微信关闭订单忽略", entity.getRechargeNo());
//                    }
//                });
//            } catch (Exception e) {
//                log.error("微信关闭充值订单异常:{}", e.getMessage());
//            }
//        });
//    }
}
