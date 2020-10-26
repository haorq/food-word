package com.meiyuan.catering.order.feign;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.meiyuan.catering.order.dto.splitbill.SubsidyFlowStatusUpdateDTO;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillSubsidyFlowEntity;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillSubsidyFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/10/12 11:23
 * @since v1.1.0
 */
@Service
public class OrdersSplitBillSubsidyFlowClint {


    @Autowired
    private CateringOrdersSplitBillSubsidyFlowService ordersSplitBillSubsidyFlowService;

    public List<CateringOrdersSplitBillSubsidyFlowEntity> listBySplitBillId(Long id, Integer status) {
        return ordersSplitBillSubsidyFlowService.listBySplitBillId(id, status);
    }

    public void updateTradeStatus(SubsidyFlowStatusUpdateDTO statusUpdateDTO) {
        ordersSplitBillSubsidyFlowService.updateTradeStatus(statusUpdateDTO);
    }

    public List<CateringOrdersSplitBillSubsidyFlowEntity> listAbnormal() {
        return ordersSplitBillSubsidyFlowService.listAbnormal();
    }

    public void saveBatch(List<CateringOrdersSplitBillSubsidyFlowEntity> subsidyFlowList) {
        ordersSplitBillSubsidyFlowService.saveBatch(subsidyFlowList);
    }

    public void updateToWaiting(Long billId, String failMsg) {
        ordersSplitBillSubsidyFlowService.updateToWaiting(billId, failMsg);
    }

    public void save(CateringOrdersSplitBillSubsidyFlowEntity subsidyFlowEntity) {
        ordersSplitBillSubsidyFlowService.save(subsidyFlowEntity);
    }

    public void removeBySplitBillId(Long billId) {
        LambdaUpdateWrapper<CateringOrdersSplitBillSubsidyFlowEntity> subsidyWrapper = new LambdaUpdateWrapper<>();
        subsidyWrapper.eq(CateringOrdersSplitBillSubsidyFlowEntity::getSplitBillId, billId);
        ordersSplitBillSubsidyFlowService.remove(subsidyWrapper);
    }
}
