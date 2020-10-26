package com.meiyuan.catering.core.dto.order.audit;

import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/5/20 13:47
 * @since v1.1.0
 */
@Data
public class OrdersRefundAudit extends IdEntity {
    /**
     * 退款表Id
     */
    private Long refundId;
    /**
     * 退款编号
     */
    private String refundNumber;
    /**
     * 退款发起时间
     */
    private LocalDateTime refundStartTime;
    /**
     * 退款原因 1:包装损坏 2:商品质量问题 3:未按约定时间送达
     */
    private List<Integer> refundReason;
    /**
     * 货物状态 1:已收到货 2:未收到货
     */
    private Integer cargoStatus;
    /**
     * 退款说明
     */
    private String refundRemark;
    /**
     * 退款凭证(图片地址:最多3张)
     */
    private List<String> refundEvidence;
    /**
     * 退款操作人员id
     */
    private Long businessOperatorId;
    /**
     * 退款操作人员名称
     */
    private String businessOperatorName;
    /**
     * 商家操作人员电话
     */
    private String businessOperatorPhone;
    /**
     * 商家ID
     */
    private Long businessId;
    /**
     * 商家名称
     */
    private String businessName;
    /**
     * 商家审核结果（1：待审核，2：通过；3：拒绝）
     */
    private Integer businessAuditStatus;
    /**
     * 审核时间
     */
    private LocalDateTime businessAuditTime;
    /**
     * 审核说明
     */
    private String businessAuditExplain;
    /**
     * 平台退款操作人员id
     */
    private Long platformOperatorId;
    /**
     * 平台退款操作人员名称
     */
    private String platformOperatorName;
    /**
     * 平台操作人员电话
     */
    private String platformOperatorPhone;
    /**
     * 平台审核结果（1：待审核，2：通过；3：拒绝）
     */
    private Integer platformAuditStatus;
    /**
     * 平台审核时间
     */
    private LocalDateTime platformAuditTime;
    /**
     * 平台审核说明
     */
    private String platformAuditExplain;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
