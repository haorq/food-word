package com.meiyuan.catering.finance.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.pay.ChargeOrder;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.FinanceUtil;
import com.meiyuan.catering.finance.dao.CateringUserChargeOrderMapper;
import com.meiyuan.catering.finance.entity.CateringUserChargeOrderEntity;
import com.meiyuan.catering.finance.enums.RechargeOrderStatusEnum;
import com.meiyuan.catering.finance.service.CateringUserChargeOrderService;
import com.meiyuan.catering.finance.util.RechargeOrderUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
@Service
public class CateringUserChargeOrderServiceImpl extends ServiceImpl<CateringUserChargeOrderMapper, CateringUserChargeOrderEntity> implements CateringUserChargeOrderService {


    @Override
    public ChargeOrder getByRechargeNo(String rechargeNo) {
        LambdaQueryWrapper<CateringUserChargeOrderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringUserChargeOrderEntity::getRechargeNo, rechargeNo);
        return BaseUtil.objToObj(baseMapper.selectOne(wrapper), ChargeOrder.class);
    }


    @Override
    public List<ChargeOrder> autoOrderCancle() {
        LambdaQueryWrapper<CateringUserChargeOrderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringUserChargeOrderEntity::getStatus, RechargeOrderStatusEnum.WAIT_PAY.getStatus());
        List<CateringUserChargeOrderEntity> list = this.list(wrapper);
        if (list.size() > 0) {
            list.forEach(i -> i.setStatus(RechargeOrderStatusEnum.CANCLE.getStatus()));
            updateBatchById(list);
        }
        return BaseUtil.objToObj(list, ChargeOrder.class);
    }

    @Override
    public ChargeOrder getOrderById(Long id) {
        CateringUserChargeOrderEntity entity = baseMapper.selectById(id);
        return BaseUtil.objToObj(entity, ChargeOrder.class);
    }

    @Override
    public Boolean updateChargeOrder(Long id, BigDecimal receivedAmount, BigDecimal cashCoupon, String transactionId) {
        CateringUserChargeOrderEntity order = new CateringUserChargeOrderEntity();
        order.setId(id);
        order.setStatus(RechargeOrderUtil.PAY_SUCCESS);
        order.setReceivedAmount(receivedAmount);
        order.setCashCoupon(cashCoupon);
        order.setRechargeTraceNo(transactionId);
        return updateById(order);
    }

    @Override
    public Boolean saveChargeOrder(Long userId, Integer userType, Long ruleId, String rechargeOrderNo, BigDecimal rechargeAccount, BigDecimal givenAccount, String ip) {
        CateringUserChargeOrderEntity orderEntity = new CateringUserChargeOrderEntity();
        orderEntity.setUserId(userId);
        orderEntity.setRechargeRuleId(ruleId);
        orderEntity.setRechargeNo(rechargeOrderNo);
        //实收金额
        orderEntity.setReceivedAmount(rechargeAccount);
        //应收金额
        orderEntity.setPayableAmount(rechargeAccount);
        //现金劵/赠送金额
        orderEntity.setCashCoupon(givenAccount);
        //总金额
        orderEntity.setTotalAmount(rechargeAccount);
        orderEntity.setOperateIp(ip);
        //是否有优惠
        orderEntity.setType(FinanceUtil.isCashDiscounts(givenAccount.compareTo(BigDecimal.ZERO)));
        orderEntity.setUserType(userType);
        return save(orderEntity);
    }
}

