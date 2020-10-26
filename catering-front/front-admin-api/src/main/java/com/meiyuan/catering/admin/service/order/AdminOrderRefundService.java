package com.meiyuan.catering.admin.service.order;

import com.meiyuan.catering.core.dto.order.audit.OrdersRefundAudit;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.query.refund.RefundQueryDTO;
import com.meiyuan.catering.order.enums.RefundReasonEnum;
import com.meiyuan.catering.order.enums.RefundWayEnum;
import com.meiyuan.catering.order.feign.OrderRefundAuditClient;
import com.meiyuan.catering.order.feign.OrderRefundClient;
import com.meiyuan.catering.order.vo.RefundQueryDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 订单退款服务
 *
 * @author zengzhangni
 * @date 2020-03-19
 */
@Service
public class AdminOrderRefundService {

    @Autowired
    private OrderRefundClient refundClient;
    @Autowired
    private OrderRefundAuditClient refundAuditClient;

    public Result<Map<String, Object>> pageList(RefundQueryDTO dto) {
        return refundClient.pageListAndRefundCount(dto);
    }

    public Result<RefundQueryDetailVO> detailById(Long id) {
        RefundOrder entity = refundClient.getRefundOrderById(id).getData();
        RefundQueryDetailVO vo = new RefundQueryDetailVO();
        vo.setOrderId(entity.getOrderId().toString());
        vo.setRefundNumber(entity.getRefundNumber());
        vo.setRefundStatus(entity.getRefundStatus());
        vo.setOrderNumber(entity.getOrderNumber());
        vo.setCreateTime(entity.getCreateTime());
        vo.setMemberName(entity.getMemberName());
        vo.setRefundWay(entity.getRefundWay());
        vo.setRefundWayDesc(RefundWayEnum.parse(vo.getRefundWay()));
        vo.setRefundAmount(entity.getRefundAmount());
        vo.setReceivedAmount(entity.getRefundAmount());

        OrdersRefundAudit auditEntity = refundAuditClient.getOneByRefundId(entity.getId()).getData();
        if (auditEntity != null) {
            vo.setRefundReason(auditEntity.getRefundReason().get(0));
            vo.setRefundReasonDesc(RefundReasonEnum.parse(vo.getRefundReason()));
            vo.setRefundRemark(auditEntity.getRefundRemark());
            vo.setBusinessName(auditEntity.getBusinessName());
            vo.setUpdateTime(auditEntity.getBusinessAuditTime());
            vo.setAuditTime(auditEntity.getBusinessAuditTime());
            vo.setBusinessRemark(auditEntity.getBusinessAuditExplain());
            vo.setBusinessAuditStatus(auditEntity.getBusinessAuditStatus());
        }

        return Result.succ(vo);
    }

}
