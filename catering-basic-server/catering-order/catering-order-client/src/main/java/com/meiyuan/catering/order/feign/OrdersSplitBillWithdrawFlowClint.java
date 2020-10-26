package com.meiyuan.catering.order.feign;

import com.meiyuan.catering.order.dto.splitbill.WithdrawFlowStatusUpdateDTO;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillWithdrawFlowEntity;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillWithdrawFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zengzhangni
 * @date 2020/10/12 11:23
 * @since v1.1.0
 */
@Service
public class OrdersSplitBillWithdrawFlowClint {


    @Autowired
    private CateringOrdersSplitBillWithdrawFlowService ordersSplitBillWithdrawFlowService;

    public void insert(CateringOrdersSplitBillWithdrawFlowEntity insertEntity) {
        ordersSplitBillWithdrawFlowService.insert(insertEntity);
    }

    public void updateTradeStatus(WithdrawFlowStatusUpdateDTO updateDTO) {
        ordersSplitBillWithdrawFlowService.updateTradeStatus(updateDTO);
    }
}
