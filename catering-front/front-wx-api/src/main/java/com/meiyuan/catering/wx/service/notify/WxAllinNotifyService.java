package com.meiyuan.catering.wx.service.notify;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.allinpay.model.bean.notify.NotifyResult;
import com.meiyuan.catering.allinpay.model.bean.notify.OrderNotifyResult;
import com.meiyuan.catering.allinpay.model.bean.notify.WithdrawNotifyResult;
import com.meiyuan.catering.order.feign.OrdersSplitBillOrderFlowClint;
import com.meiyuan.catering.pay.util.PaySupport;
import com.meiyuan.catering.pay.util.PayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zengzhangni
 * @date 2020/10/9 10:11
 * @since v1.1.0
 */
@Service
@Slf4j
public class WxAllinNotifyService {

    @Autowired
    private PaySupport support;
    @Autowired
    private OrdersSplitBillOrderFlowClint ordersSplitBillOrderFlowClint;

    public Object agentPayNotify(String data) {

        log.info("分账结果通知:{}", data);
        NotifyResult notifyInfo = PayUtil.getNotifyInfo(data);
        OrderNotifyResult result = notifyInfo.getResult();
        log.info("notifyInfo:{}", notifyInfo);
        Long splitBillId = Long.valueOf(result.getExtendInfo());
        String orderNo = result.getOrderNo();

        ordersSplitBillOrderFlowClint.updateToSuccess(splitBillId, orderNo, data);

        return "success";
    }

    public void withdrawApplyNotify(String result) {
        log.info("提现结果通知：{}", result);
        NotifyResult notifyInfo = PayUtil.getNotifyInfo(result);
        OrderNotifyResult info = notifyInfo.getResult();
        String notifyStr = JSON.toJSONString(info);
        log.info("notify:{}", notifyStr);
        support.withdrawApplyDispose(info, notifyStr);

    }
}
