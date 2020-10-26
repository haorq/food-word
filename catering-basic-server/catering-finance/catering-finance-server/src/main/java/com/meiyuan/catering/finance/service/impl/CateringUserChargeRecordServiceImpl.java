package com.meiyuan.catering.finance.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.pay.ChargeOrder;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.finance.dao.CateringUserChargeRecordMapper;
import com.meiyuan.catering.finance.entity.CateringUserChargeRecordEntity;
import com.meiyuan.catering.finance.query.recharge.RechargeRecordQueryDTO;
import com.meiyuan.catering.finance.service.CateringUserChargeRecordService;
import com.meiyuan.catering.finance.vo.recharge.RechargeRecordListVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
@Service
public class CateringUserChargeRecordServiceImpl extends ServiceImpl<CateringUserChargeRecordMapper, CateringUserChargeRecordEntity> implements CateringUserChargeRecordService {

    @Override
    public IPage<RechargeRecordListVO> pageList(RechargeRecordQueryDTO dto, List<Long> userIds) {
        LocalDateTime endTime = dto.getEndTime();
        dto.setEndTime(endTime != null ? endTime.plusDays(1) : null);
        return baseMapper.pageList(dto.getPage(), dto, userIds);
    }

    @Override
    public Boolean saveChargeRecord(ChargeOrder order, String rechargeNo, String transactionId) {
        CateringUserChargeRecordEntity chargeRecordEntity = new CateringUserChargeRecordEntity();
        chargeRecordEntity.setUserId(order.getUserId());
        chargeRecordEntity.setRechargeNo(rechargeNo);
        chargeRecordEntity.setRechargeTraceNo(transactionId);
        chargeRecordEntity.setReceivedAmount(order.getReceivedAmount());
        chargeRecordEntity.setPayableAmount(order.getPayableAmount());
        chargeRecordEntity.setDiscountAmount(order.getDiscountAmount());
        chargeRecordEntity.setCashCoupon(order.getCashCoupon());
        chargeRecordEntity.setTotalAmount(order.getTotalAmount());
        chargeRecordEntity.setPayWay(PayWayEnum.WX.getPayWay());
        chargeRecordEntity.setOperateIp(order.getOperateIp());
        chargeRecordEntity.setUserType(order.getUserType());
        return save(chargeRecordEntity);
    }
}

