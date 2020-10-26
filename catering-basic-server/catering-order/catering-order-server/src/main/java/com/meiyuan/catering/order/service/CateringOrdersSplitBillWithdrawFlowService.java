package com.meiyuan.catering.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.order.dto.splitbill.WithdrawFlowStatusUpdateDTO;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillWithdrawFlowEntity;

import com.meiyuan.catering.order.entity.CateringOrdersSplitBillWithdrawFlowEntity;

/**
 * @author GongJunZheng
 * @date 2020/10/09 13:10
 * @description 分账提现流水服务接口
 **/

public interface CateringOrdersSplitBillWithdrawFlowService extends IService<CateringOrdersSplitBillWithdrawFlowEntity> {

    /**
    * 保存提现信息
    * @param entity 实体信息
    * @author: GongJunZheng
    * @date: 2020/10/9 18:30
    * @return: void
    * @version V1.5.0
    **/
    void insert(CateringOrdersSplitBillWithdrawFlowEntity entity);

    /**
    * 修改提现流水状态
    * @param dto 提现流水状态修改DTO
    * @author: GongJunZheng
    * @date: 2020/10/10 9:24
    * @return: void
    * @version V1.5.0
    **/
    void updateTradeStatus(WithdrawFlowStatusUpdateDTO dto);
}
