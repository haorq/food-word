package com.meiyuan.catering.order.vo.splitbill;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/10/11 15:10
 * @description 简单描述
 **/

@Data
public class CateringOrdersSplitBillOrderFlowVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 分账流水表ID
     */
    private Long splitBillId;
    /**
     * 系统交易流水编号
     */
    private String tradingNumber;
    /**
     * 第三方交易流水编号
     */
    private String thirdTradeNumber;
    /**
     * 收款人
     */
    private String collectionUser;
    /**
     * 订单分账金额
     */
    private BigDecimal orderSplitAmount;
    /**
     * 手续费率（单位：%）
     */
    private BigDecimal feeRate;
    /**
     * 手续费
     */
    private BigDecimal fee;
    /**
     * 分账规则
     */
    private String splitRule;
    /**
     * 分账类型
     */
    private Integer splitType;
    /**
     * 交易状态（0：未交易；1：交易中；2：交易成功；3：交易失败）
     */
    private Integer tradeStatus;
    /**
     * 失败原因
     */
    private String failMessage;
    /**
     * 成功时间
     */
    private LocalDateTime successTime;
    /**
     * 通知参数信息
     */
    private String notifyMessage;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNumber;

}
