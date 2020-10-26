package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.order.dao.CateringOrdersSplitBillWithdrawFlowMapper;
import com.meiyuan.catering.order.dto.splitbill.WithdrawFlowStatusUpdateDTO;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillWithdrawFlowEntity;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillWithdrawFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author GongJunZheng
 * @date 2020/10/09 13:10
 * @description 分账提现流水服务接口实现
 **/

@Slf4j
@Service("cateringOrdersSplitBillWithdrawFlowService")
public class CateringOrdersSplitBillWithdrawFlowServiceImpl
        extends ServiceImpl<CateringOrdersSplitBillWithdrawFlowMapper, CateringOrdersSplitBillWithdrawFlowEntity> implements CateringOrdersSplitBillWithdrawFlowService {

    @Override
    public void insert(CateringOrdersSplitBillWithdrawFlowEntity entity) {
        save(entity);
    }

    @Override
    public void updateTradeStatus(WithdrawFlowStatusUpdateDTO dto) {
        CateringOrdersSplitBillWithdrawFlowEntity update = BaseUtil.objToObj(dto, CateringOrdersSplitBillWithdrawFlowEntity.class);
        updateById(update);
    }
}
