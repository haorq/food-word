package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.order.dao.CateringOrdersSplitBillSubsidyFlowMapper;
import com.meiyuan.catering.order.dto.splitbill.SubsidyFlowStatusUpdateDTO;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillSubsidyFlowEntity;
import com.meiyuan.catering.order.enums.TradeStatusEnum;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillSubsidyFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/09 13:10
 * @description 补贴分账流水服务接口实现
 **/

@Slf4j
@Service("cateringOrdersSplitBillSubsidyFlowService")
public class CateringOrdersSplitBillSubsidyFlowServiceImpl
        extends ServiceImpl<CateringOrdersSplitBillSubsidyFlowMapper, CateringOrdersSplitBillSubsidyFlowEntity> implements CateringOrdersSplitBillSubsidyFlowService {

    @Override
    public List<CateringOrdersSplitBillSubsidyFlowEntity> listBySplitBillId(Long splitBillId, Integer tradeStatus) {
        LambdaQueryWrapper<CateringOrdersSplitBillSubsidyFlowEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringOrdersSplitBillSubsidyFlowEntity::getSplitBillId, splitBillId)
                .eq(!ObjectUtils.isEmpty(tradeStatus), CateringOrdersSplitBillSubsidyFlowEntity::getTradeStatus, tradeStatus);
        return list(queryWrapper);
    }

    @Override
    public void updateTradeStatus(SubsidyFlowStatusUpdateDTO dto) {
        CateringOrdersSplitBillSubsidyFlowEntity update = BaseUtil.objToObj(dto, CateringOrdersSplitBillSubsidyFlowEntity.class);
        updateById(update);
    }

    @Override
    public Boolean updateToWaiting(Long billId, String failMsg) {
        LambdaUpdateWrapper<CateringOrdersSplitBillSubsidyFlowEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CateringOrdersSplitBillSubsidyFlowEntity::getTradeStatus, TradeStatusEnum.WAITING.getStatus())
                .set(CateringOrdersSplitBillSubsidyFlowEntity::getFailMessage, failMsg)
                .eq(CateringOrdersSplitBillSubsidyFlowEntity::getSplitBillId, billId);
        return update(wrapper);
    }

    @Override
    public List<CateringOrdersSplitBillSubsidyFlowEntity> listAbnormal() {
        return baseMapper.listCanSubsidy();
    }
}
