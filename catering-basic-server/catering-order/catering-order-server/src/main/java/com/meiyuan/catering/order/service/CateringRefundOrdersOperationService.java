package com.meiyuan.catering.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.order.audit.WxRefundAuditVO;
import com.meiyuan.catering.order.entity.CateringRefundOrdersOperationEntity;
import com.meiyuan.catering.order.enums.RefundOperationEnum;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-31
 */
public interface CateringRefundOrdersOperationService extends IService<CateringRefundOrdersOperationEntity> {


    /**
     * 描述:保存退款进度
     *
     * @param orderId
     * @param refundId
     * @param operationEnum
     * @param remarks
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/20 9:49
     * @since v1.1.0
     */
    Boolean saveOperation(Long orderId, Long refundId, RefundOperationEnum operationEnum, String remarks);

    /**
     * 描述:描述:查询通过审核信息
     *
     * @param orderId
     * @return java.util.List<com.meiyuan.catering.core.dto.order.audit.WxRefundAuditVO>
     * @author zengzhangni
     * @date 2020/5/20 16:08
     * @since v1.1.0
     */
    List<WxRefundAuditVO> refundAuditList(Long orderId);
}
