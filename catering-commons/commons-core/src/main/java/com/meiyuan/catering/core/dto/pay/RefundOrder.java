package com.meiyuan.catering.core.dto.pay;

import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/5/19 16:54
 * @since v1.1.0
 */
@Data
public class RefundOrder extends IdEntity {
    /**
     * 订单表Id
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 系统交易流水编号
     */
    private String tradingFlow;
    /**
     * 退款编号
     */
    private String refundNumber;
    /**
     * 退款人ID
     */
    private Long memberId;
    /**
     * 退款人名称
     */
    private String memberName;
    /**
     * 退款类型(1：系统退款(默认) 2：店长退款 3：运营退款 4：用户退款)
     */
    private Integer refundType;
    /**
     * 退款方式(1:原路退回)
     */
    private Integer refundWay;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 已退金额
     */
    private BigDecimal refundedAmount;
    /**
     * 退款完成时间
     */
    private LocalDateTime refundCompleteTime;
    /**
     * 退款状态（1：待退款；2：退款成功；3退款失败）
     */
    private Integer refundStatus;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 门店ID
     */
    private Long storeId;
}
