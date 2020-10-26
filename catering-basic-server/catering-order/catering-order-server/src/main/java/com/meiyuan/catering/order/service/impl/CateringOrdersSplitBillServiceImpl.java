package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.order.dao.CateringOrdersSplitBillMapper;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillEntity;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillOrderFlowEntity;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillSubsidyFlowEntity;
import com.meiyuan.catering.order.enums.TradeStatusEnum;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillOrderFlowService;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillService;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillSubsidyFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author GongJunZheng
 * @date 2020/10/09 13:10
 * @description 分账服务接口实现
 **/

@Slf4j
@Service("cateringOrdersSplitBillService")
public class CateringOrdersSplitBillServiceImpl
        extends ServiceImpl<CateringOrdersSplitBillMapper, CateringOrdersSplitBillEntity> implements CateringOrdersSplitBillService {

    @Autowired
    private CateringOrdersSplitBillOrderFlowService cateringOrdersSplitBillOrderFlowService;
    @Autowired
    private CateringOrdersSplitBillSubsidyFlowService cateringOrdersSplitBillSubsidyFlowService;

    @Override
    public CateringOrdersSplitBillEntity getByOrderId(Long orderId) {
        LambdaQueryWrapper<CateringOrdersSplitBillEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringOrdersSplitBillEntity::getOrderId, orderId)
                .eq(CateringOrdersSplitBillEntity::getTradeStatus, TradeStatusEnum.NOT.getStatus());
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public CateringOrdersSplitBillEntity getById(Long id) {
        LambdaQueryWrapper<CateringOrdersSplitBillEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringOrdersSplitBillEntity::getId, id);
        return baseMapper.selectOne(queryWrapper);
    }


    @Override
    public Boolean updateToWaiting(Long id) {
        LambdaUpdateWrapper<CateringOrdersSplitBillEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CateringOrdersSplitBillEntity::getTradeStatus, TradeStatusEnum.WAITING.getStatus())
                .eq(CateringOrdersSplitBillEntity::getId, id);
        return update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteSplitBill(Long orderId) {
        CateringOrdersSplitBillEntity billEntity = getByOrderId(orderId);
        Long billId = billEntity.getId();

        //删除订单分账信息
        LambdaUpdateWrapper<CateringOrdersSplitBillOrderFlowEntity> orderWrapper = new LambdaUpdateWrapper<>();
        orderWrapper.eq(CateringOrdersSplitBillOrderFlowEntity::getSplitBillId, billId);
        cateringOrdersSplitBillOrderFlowService.remove(orderWrapper);

        //删除补贴信息
        LambdaUpdateWrapper<CateringOrdersSplitBillSubsidyFlowEntity> subsidyWrapper = new LambdaUpdateWrapper<>();
        subsidyWrapper.eq(CateringOrdersSplitBillSubsidyFlowEntity::getSplitBillId, billId);
        cateringOrdersSplitBillSubsidyFlowService.remove(subsidyWrapper);

        //删除主分账信息
        return removeById(billId);
    }
}
