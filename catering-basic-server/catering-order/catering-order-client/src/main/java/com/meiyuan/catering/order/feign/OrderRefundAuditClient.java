package com.meiyuan.catering.order.feign;


import com.meiyuan.catering.core.dto.order.audit.OrdersRefundAudit;
import com.meiyuan.catering.core.dto.pay.wx.RefundDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.enums.AuditStatusEnum;
import com.meiyuan.catering.order.service.CateringOrdersRefundAuditService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/5/20 9:39
 * @since v1.1.0
 */
@Service
public class OrderRefundAuditClient {
    @Resource
    private CateringOrdersRefundAuditService service;

    /**
     * 描述:添加退款审核
     *
     * @param shopId
     * @param shopName
     * @param refundNo
     * @param refundId
     * @param refundDTO
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/20 9:41
     * @since v1.1.0
     */
    public Result<Boolean> saveAudit(Long shopId, String shopName, String refundNo, Long refundId, RefundDTO refundDTO) {
        return Result.succ(service.saveAudit(shopId, shopName, refundNo, refundId, refundDTO));
    }

    /**
     * 描述:更新退款审核
     *
     * @param shopId
     * @param refundId
     * @param auditStatusEnum
     * @param auditExplain
     * @param shopName
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/20 10:30
     * @since v1.1.0
     */
    public Result<Boolean> updateAudit(Long shopId, Long refundId, AuditStatusEnum auditStatusEnum, String auditExplain, String shopName) {
        return Result.succ(service.updateAudit(shopId, refundId, auditStatusEnum, auditExplain, shopName));
    }

    /**
     * 描述:查询退款审核信息
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.dto.order.audit.OrdersRefundAudit>
     * @author zengzhangni
     * @date 2020/5/20 13:49
     * @since v1.1.0
     */
    public Result<OrdersRefundAudit> getOneByRefundId(Long id) {
        return Result.succ(service.getOneByRefundId(id));
    }
}
