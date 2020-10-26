package com.meiyuan.catering.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillOrderFlowEntity;
import com.meiyuan.catering.order.vo.splitbill.CateringOrdersSplitBillOrderFlowVO;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/09 13:10
 * @description 订单分账流水服务接口
 **/

public interface CateringOrdersSplitBillOrderFlowService extends IService<CateringOrdersSplitBillOrderFlowEntity> {

    /**
     * 根据分账流水ID查询订单分账流水集合
     *
     * @param splitBillId 分账流水ID
     * @author: GongJunZheng
     * @date: 2020/10/9 16:30
     * @return: {@link List<CateringOrdersSplitBillOrderFlowEntity>}
     * @version V1.5.0
     **/
    List<CateringOrdersSplitBillOrderFlowEntity> listBySplitBillId(Long splitBillId);

    List<CateringOrdersSplitBillOrderFlowEntity> listByBillId(Long billEntityId);

    /**
     * 描述:更新交易状态为待交易
     *
     * @param billId
     * @param failMsg
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/10 9:37
     * @since v1.5.0
     */
    Boolean updateToWaiting(Long billId, String failMsg);

    /**
     * 描述:更新交易状态为完成
     *
     * @param splitBillId
     * @param thirdTradeNumber
     * @param notifyMessage
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/10 10:43
     * @since v1.5.0
     */
    Boolean updateToSuccess(Long splitBillId, String thirdTradeNumber, String notifyMessage);

    /**
     * 查询异常订单分账信息（失败+待交易）
     *
     * @author: GongJunZheng
     * @date: 2020/10/10 11:26
     * @return: {@link List<CateringOrdersSplitBillOrderFlowVO>}
     * @version V1.5.0
     **/
    List<CateringOrdersSplitBillOrderFlowVO> listAbnormal();
}
