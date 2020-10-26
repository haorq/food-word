package com.meiyuan.catering.order.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillOrderFlowEntity;
import com.meiyuan.catering.order.enums.SplitTypeEnum;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillOrderFlowService;
import com.meiyuan.catering.order.vo.splitbill.CateringOrdersSplitBillOrderFlowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/10/12 11:23
 * @since v1.1.0
 */
@Service
public class OrdersSplitBillOrderFlowClint {

    @Autowired
    private CateringOrdersSplitBillOrderFlowService ordersSplitBillOrderFlowService;

    public List<CateringOrdersSplitBillOrderFlowEntity> listByBillId(Long billEntityId) {
        return ordersSplitBillOrderFlowService.listByBillId(billEntityId);
    }

    public List<CateringOrdersSplitBillOrderFlowVO> listAbnormal() {
        return ordersSplitBillOrderFlowService.listAbnormal();
    }

    public void saveBatch(List<CateringOrdersSplitBillOrderFlowEntity> orderFlowList) {
        ordersSplitBillOrderFlowService.saveBatch(orderFlowList);
    }

    public void updateToWaiting(Long billId, String failMsg) {
        ordersSplitBillOrderFlowService.updateToWaiting(billId, failMsg);
    }

    public void updateToSuccess(Long splitBillId, String orderNo, String notifyMessage) {
        ordersSplitBillOrderFlowService.updateToSuccess(splitBillId, orderNo, notifyMessage);
    }

    public void removeBySplitBillId(Long billId) {
        LambdaUpdateWrapper<CateringOrdersSplitBillOrderFlowEntity> orderWrapper = new LambdaUpdateWrapper<>();
        orderWrapper.eq(CateringOrdersSplitBillOrderFlowEntity::getSplitBillId, billId);
        ordersSplitBillOrderFlowService.remove(orderWrapper);
    }

    public List<CateringOrdersSplitBillOrderFlowEntity> queryInnerList(Long billId) {
        LambdaQueryWrapper<CateringOrdersSplitBillOrderFlowEntity> query = new LambdaQueryWrapper<>();
        query.eq(CateringOrdersSplitBillOrderFlowEntity::getSplitBillId, billId);
        query.eq(CateringOrdersSplitBillOrderFlowEntity::getSplitType, SplitTypeEnum.INNER_BUCKLE.getType());
        return ordersSplitBillOrderFlowService.list(query);
    }
}
