package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.order.audit.OrdersRefundAudit;
import com.meiyuan.catering.core.dto.pay.wx.RefundDTO;
import com.meiyuan.catering.order.entity.CateringOrdersRefundAuditEntity;
import com.meiyuan.catering.order.enums.AuditStatusEnum;

import java.util.List;

/**
 * 退款订单审核表(CateringOrdersRefundAudit)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:04
 */
public interface CateringOrdersRefundAuditService extends IService<CateringOrdersRefundAuditEntity> {

    /**
     * 描述:通过退款id 查询退款审核列表
     *
     * @param id
     * @return java.util.List<com.meiyuan.catering.core.dto.order.audit.OrdersRefundAudit>
     * @author zengzhangni
     * @date 2020/5/20 13:52
     * @since v1.1.0
     */
    List<OrdersRefundAudit> getByRefundId(Long id);

    /**
     * 描述:通过退款id 查询退款审核
     *
     * @param id
     * @return com.meiyuan.catering.core.dto.order.audit.OrdersRefundAudit
     * @author zengzhangni
     * @date 2020/5/20 13:52
     * @since v1.1.0
     */
    OrdersRefundAudit getOneByRefundId(Long id);


    /**
     * 描述:添加退款审核
     *
     * @param shopId
     * @param shopName
     * @param refundNo
     * @param refundId
     * @param refundDTO
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/20 9:37
     * @since v1.1.0
     */
    Boolean saveAudit(Long shopId, String shopName, String refundNo, Long refundId, RefundDTO refundDTO);

    /**
     * 描述:更新退款审核
     *
     * @param shopId
     * @param refundId
     * @param auditStatusEnum
     * @param auditExplain
     * @param shopName
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/20 10:29
     * @since v1.1.0
     */
    Boolean updateAudit(Long shopId, Long refundId, AuditStatusEnum auditStatusEnum, String auditExplain, String shopName);
}
