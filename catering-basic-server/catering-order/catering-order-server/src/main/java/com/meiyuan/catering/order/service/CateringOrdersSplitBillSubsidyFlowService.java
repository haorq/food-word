package com.meiyuan.catering.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.order.dto.splitbill.SubsidyFlowStatusUpdateDTO;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillSubsidyFlowEntity;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/09 13:10
 * @description 补贴分账流水服务接口
 **/

public interface CateringOrdersSplitBillSubsidyFlowService extends IService<CateringOrdersSplitBillSubsidyFlowEntity> {

    /**
     * 根据分账流水表ID查询需要补贴转账的信息集合
     *
     * @param splitBillId 分账流水表ID
     * @param tradeStatus 查询状态
     * @author: GongJunZheng
     * @date: 2020/10/9 15:16
     * @return: {@link List<CateringOrdersSplitBillSubsidyFlowEntity>}
     * @version V1.5.0
     **/
    List<CateringOrdersSplitBillSubsidyFlowEntity> listBySplitBillId(Long splitBillId, Integer tradeStatus);

    /**
     * 修改分账流水状态
     *
     * @param dto 分账流水状态修改DTO
     * @author: GongJunZheng
     * @date: 2020/10/9 15:40
     * @return: void
     * @version V1.5.0
     **/
    void updateTradeStatus(SubsidyFlowStatusUpdateDTO dto);

    /**
     * 描述:更新交易状态为待交易
     *
     * @param billId
     * @param failMsg
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/10 9:47
     * @since v1.5.0
     */
    Boolean updateToWaiting(Long billId, String failMsg);

    /**
    * 查询异常订单分账信息（失败+待交易）
    * @author: GongJunZheng
    * @date: 2020/10/10 11:52
    * @return: {@link List<CateringOrdersSplitBillSubsidyFlowEntity>}
    * @version V1.5.0
    **/
    List<CateringOrdersSplitBillSubsidyFlowEntity> listAbnormal();

}
