package com.meiyuan.catering.pay.util;

import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.SubsidyPayDTO;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.entity.*;
import com.meiyuan.catering.order.enums.AmountTypeEnum;
import com.meiyuan.catering.order.enums.DeliveryRemarkEnum;
import com.meiyuan.catering.order.feign.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/10/14 17:27
 * @since v1.5.0
 */
@Component
@Slf4j
public class AllinHelper {

    @Autowired
    private OrderClient orderClient;
    @Autowired
    private OrdersSplitBillClint ordersSplitBillClint;
    @Autowired
    private OrdersSplitBillOrderFlowClint ordersSplitBillOrderFlowClint;
    @Autowired
    private OrdersSplitBillSubsidyFlowClint ordersSplitBillSubsidyFlowClint;
    @Autowired
    private OrdersShopDebtClient ordersShopDebtClient;
    @Autowired
    private OrdersShopDebtFlowClient ordersShopDebtFlowClient;
    @Autowired
    private MerchantUtils merchantUtils;

    /**
     * 描述: 配送完成,添加负债信息
     *
     * @param orderId
     * @param debtAmount
     * @return void
     * @author zengzhangni
     * @date 2020/10/15 14:34
     * @since v1.5.0
     */
    @Transactional(rollbackFor = Exception.class)
    public void disposeDelivery(Long orderId, BigDecimal debtAmount) {
        //查询是否为首次配送
        Boolean isFirstDelivery = isFirstDelivery(orderId);

        dispose(orderId, debtAmount, isFirstDelivery);
    }


    private void dispose(Long orderId, BigDecimal debtAmount, Boolean isFirstDelivery) {
        // 根据orderId查询用户支付的配送费
        Result<Order> orderResult = orderClient.getOrderById(orderId);
        Order order = orderResult.getData();
        if (order == null) {
            throw new CustomException("订单不存在");
        }
        BigDecimal deliveryPrice = order.getDeliveryPrice();
        Long shopId = order.getStoreId();

        // 计算补贴
        SubsidyPayDTO subsidyPayDTO = merchantUtils.subsidyAmount(shopId, deliveryPrice, debtAmount, isFirstDelivery);
        BigDecimal platformAmount = subsidyPayDTO.getPlatformAmount();

        // 平台获取的配送费大于，存入数据库
        if (BaseUtil.isGtZero(platformAmount)) {
            //累加负债信息
            ordersShopDebtClient.accumulate(shopId, platformAmount);
            //添加负债明细
            saveDebtFlow(orderId, shopId, debtAmount, subsidyPayDTO, AmountTypeEnum.COPE_WITH, "平台收取配送费");
        }
    }

    private Boolean isFirstDelivery(Long orderId) {
        // 查询配送次数 是否=0  ==0说明是首次配送
        return ordersShopDebtFlowClient.isFirstDelivery(orderId);
    }


    private void saveDebtFlow(Long shopId, Long orderId, BigDecimal debtAmount, DeliveryRemarkEnum remarkEnum) {
        //累加负债信息
        ordersShopDebtClient.accumulate(shopId, debtAmount);
        //添加负债明细
        saveDebtFlow(orderId, shopId, debtAmount, AmountTypeEnum.COPE_WITH, remarkEnum.getRemark());
    }


    private void saveDebtFlow(Long orderId, Long shopId, BigDecimal debtAmount, AmountTypeEnum amountType, String remark) {
        CateringOrdersShopDebtEntity debtEntity = queryByShopId(shopId);
        saveDebtFlow(orderId, shopId, debtEntity.getId(), debtAmount, amountType, remark);
    }

    private void saveDebtFlow(Long orderId, Long shopId, BigDecimal debtAmount, SubsidyPayDTO subsidyPayDTO, AmountTypeEnum amountType, String remark) {
        CateringOrdersShopDebtEntity debtEntity = queryByShopId(shopId);
        CateringOrdersShopDebtFlowEntity debtFlowEntity = new CateringOrdersShopDebtFlowEntity();
        debtFlowEntity.setShopDebtId(debtEntity.getId());
        debtFlowEntity.setShopId(shopId);
        debtFlowEntity.setOrderId(orderId);
        debtFlowEntity.setDebtAmount(debtAmount);
        debtFlowEntity.setSubsidyAmount(subsidyPayDTO.getSubsidyAmount());
        debtFlowEntity.setAmount(subsidyPayDTO.getPlatformAmount());
        debtFlowEntity.setDebtType(1);
        debtFlowEntity.setAmountType(amountType.getType());
        debtFlowEntity.setRemarks(remark);
        ordersShopDebtFlowClient.save(debtFlowEntity);
    }

    private void saveDebtFlow(Long orderId, Long shopId, Long debtEntityId, BigDecimal debtAmount, AmountTypeEnum amountType, String remark) {
        CateringOrdersShopDebtFlowEntity debtFlowEntity = new CateringOrdersShopDebtFlowEntity();
        debtFlowEntity.setShopDebtId(debtEntityId);
        debtFlowEntity.setShopId(shopId);
        debtFlowEntity.setOrderId(orderId);
        debtFlowEntity.setAmount(debtAmount);
        debtFlowEntity.setDebtType(1);
        debtFlowEntity.setAmountType(amountType.getType());
        debtFlowEntity.setRemarks(remark);
        ordersShopDebtFlowClient.save(debtFlowEntity);
    }

    private CateringOrdersShopDebtEntity queryByShopId(Long shopId) {
        return ordersShopDebtClient.queryByShopId(shopId);
    }

    public void deleteSplitBill(Long orderId, Long shopId) {
        CateringOrdersSplitBillEntity billEntity = ordersSplitBillClint.getByOrderId(orderId);
        Long billId = billEntity.getId();

        //内扣
        List<CateringOrdersSplitBillOrderFlowEntity> entities = ordersSplitBillOrderFlowClint.queryInnerList(billId);
        if (entities.size() > 0) {
            //添加负债信息
            BigDecimal innerAmount = entities.stream().map(CateringOrdersSplitBillOrderFlowEntity::getOrderSplitAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            saveDebtFlow(shopId, orderId, innerAmount, DeliveryRemarkEnum.REFUND);
        }


        //删除订单分账信息
        ordersSplitBillOrderFlowClint.removeBySplitBillId(billId);
        //删除补贴信息
        ordersSplitBillSubsidyFlowClint.removeBySplitBillId(billId);
        //删除主分账信息
        ordersSplitBillClint.removeById(billId);
    }
}
