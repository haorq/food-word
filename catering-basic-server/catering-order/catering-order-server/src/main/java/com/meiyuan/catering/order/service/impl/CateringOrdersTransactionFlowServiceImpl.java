package com.meiyuan.catering.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.util.FinanceUtil;
import com.meiyuan.catering.order.dao.CateringOrdersTransactionFlowMapper;
import com.meiyuan.catering.order.entity.CateringOrdersTransactionFlowEntity;
import com.meiyuan.catering.order.enums.TradeStatusEnum;
import com.meiyuan.catering.order.enums.TransactionFlowEnum;
import com.meiyuan.catering.order.service.CateringOrdersTransactionFlowService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单交易流水表(CateringOrdersTransactionFlow)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Service("cateringOrdersTransactionFlowService")
public class CateringOrdersTransactionFlowServiceImpl extends ServiceImpl<CateringOrdersTransactionFlowMapper, CateringOrdersTransactionFlowEntity> implements CateringOrdersTransactionFlowService {

    @Override
    public Boolean saveOrdersTransactionFlow(Order order, String xmlResult, String transactionId, PayWayEnum payWayEnum, TransactionFlowEnum flowEnum) {
        CateringOrdersTransactionFlowEntity flowEntity = new CateringOrdersTransactionFlowEntity();
        flowEntity.setOrderId(order.getId());
        flowEntity.setOrderNumber(order.getOrderNumber());
        flowEntity.setType(flowEnum.getStatus());
        flowEntity.setTradingFlow(StringUtils.isNotBlank(order.getTradingFlow()) ? order.getTradingFlow() : FinanceUtil.systemTransactionNo());
        flowEntity.setThirdTradeNo(transactionId);
        flowEntity.setPayWay(payWayEnum.getPayWay());
        flowEntity.setPayAmount(order.getDiscountLaterFee());
        flowEntity.setPaidAmount(order.getDiscountLaterFee());
        flowEntity.setPaidTime(order.getPaidTime() == null ? LocalDateTime.now() : order.getPaidTime());
        flowEntity.setPayDeadline(order.getPayDeadline());
        flowEntity.setTradeStatus(TradeStatusEnum.SUCCESS.getStatus());
        flowEntity.setPaymenInfo(JSONObject.toJSONString(xmlResult));
        flowEntity.setChannelRate(BigDecimal.ZERO);
        return save(flowEntity);
    }

    @Override
    public Boolean saveOrdersTransactionFlow(RefundOrder refundOrder, String xmlResult, String transactionId, PayWayEnum payWayEnum) {
        CateringOrdersTransactionFlowEntity flowEntity = new CateringOrdersTransactionFlowEntity();
        flowEntity.setOrderId(refundOrder.getId());
        flowEntity.setOrderNumber(refundOrder.getOrderNumber());
        flowEntity.setThirdTradeNo(transactionId);
        flowEntity.setPaidAmount(refundOrder.getRefundAmount());
        flowEntity.setPayAmount(refundOrder.getRefundAmount());
        flowEntity.setPaidTime(refundOrder.getRefundCompleteTime());
        return saveOrdersTransactionFlow(flowEntity, xmlResult, payWayEnum);
    }

    private Boolean saveOrdersTransactionFlow(CateringOrdersTransactionFlowEntity flowEntity, String xmlResult, PayWayEnum payWayEnum) {
        flowEntity.setTradingFlow(FinanceUtil.systemTransactionNo());
        flowEntity.setType(TransactionFlowEnum.REFUND.getStatus());
        flowEntity.setChannelRate(BigDecimal.ZERO);
        flowEntity.setTradeStatus(TradeStatusEnum.SUCCESS.getStatus());
        flowEntity.setPaymenInfo(JSONObject.toJSONString(xmlResult));
        flowEntity.setPayWay(payWayEnum.getPayWay());
        return save(flowEntity);
    }
}
