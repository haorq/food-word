package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.order.dao.CateringOrdersSplitBillOrderFlowMapper;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillOrderFlowEntity;
import com.meiyuan.catering.order.enums.TradeStatusEnum;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillOrderFlowService;
import com.meiyuan.catering.order.vo.splitbill.CateringOrdersSplitBillOrderFlowVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/09 13:10
 * @description 订单分账流水服务接口实现
 **/

@Slf4j
@Service("cateringOrdersSplitBillOrderFlowService")
public class CateringOrdersSplitBillOrderFlowServiceImpl
        extends ServiceImpl<CateringOrdersSplitBillOrderFlowMapper, CateringOrdersSplitBillOrderFlowEntity> implements CateringOrdersSplitBillOrderFlowService {

    @Override
    public List<CateringOrdersSplitBillOrderFlowEntity> listBySplitBillId(Long splitBillId) {
        LambdaQueryWrapper<CateringOrdersSplitBillOrderFlowEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringOrdersSplitBillOrderFlowEntity::getSplitBillId, splitBillId)
                .eq(CateringOrdersSplitBillOrderFlowEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return list(queryWrapper);
    }

    @Override
    public List<CateringOrdersSplitBillOrderFlowEntity> listByBillId(Long billEntityId) {
        LambdaQueryWrapper<CateringOrdersSplitBillOrderFlowEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersSplitBillOrderFlowEntity::getSplitBillId, billEntityId);
        return list(wrapper);
    }

    @Override
    public Boolean updateToWaiting(Long billId, String failMsg) {
        LambdaUpdateWrapper<CateringOrdersSplitBillOrderFlowEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CateringOrdersSplitBillOrderFlowEntity::getTradeStatus, TradeStatusEnum.WAITING.getStatus())
                .set(CateringOrdersSplitBillOrderFlowEntity::getFailMessage, failMsg)
                .eq(CateringOrdersSplitBillOrderFlowEntity::getSplitBillId, billId);
        return update(wrapper);
    }

    @Override
    public List<CateringOrdersSplitBillOrderFlowVO> listAbnormal() {
        return baseMapper.listCanSplitBill();
    }

    @Override
    public Boolean updateToSuccess(Long splitBillId, String thirdTradeNumber, String notifyMessage) {
        LambdaUpdateWrapper<CateringOrdersSplitBillOrderFlowEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CateringOrdersSplitBillOrderFlowEntity::getTradeStatus, TradeStatusEnum.SUCCESS.getStatus())
                .set(CateringOrdersSplitBillOrderFlowEntity::getThirdTradeNumber, thirdTradeNumber)
                .set(CateringOrdersSplitBillOrderFlowEntity::getSuccessTime, LocalDateTime.now())
                .set(CateringOrdersSplitBillOrderFlowEntity::getNotifyMessage, notifyMessage)
                .eq(CateringOrdersSplitBillOrderFlowEntity::getSplitBillId, splitBillId);
        return update(wrapper);
    }
}
