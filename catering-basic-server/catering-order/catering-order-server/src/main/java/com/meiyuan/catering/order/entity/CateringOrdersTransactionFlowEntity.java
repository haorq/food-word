package com.meiyuan.catering.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

/**
 * 订单交易流水表(CateringOrdersTransactionFlow)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@TableName("catering_orders_transaction_flow")
public class CateringOrdersTransactionFlowEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = -78819955443500573L;

    /** 订单ID */
    private Long orderId;
    /** 订单编号 */
    private String orderNumber;
    /** 交易流水类型（1：订单支付收入；2：订单退款支出） */
    private Integer type;
    /** 系统交易流水编号 */
    private String tradingFlow;
    /** 第三方交易流水编号 */
    private String thirdTradeNo;
    /** 支付方式（ 1：微信支付（WX)； 2：支付宝支付(ALP)；3：POS刷卡支付(YH)；4：会员卡支付(HYK)；5：优惠券支付(YHQ)；6：现金支付(XJ)；7：线下支付） */
    private Integer payWay;
    /** 支付金额 */
    private BigDecimal payAmount;
    /** 已支付金额 */
    private BigDecimal paidAmount;
    /** 渠道费率 */
    private BigDecimal channelRate;
    /** 支付时间 */
    private LocalDateTime paidTime;
    /** 交易状态（1：交易中；2：交易成功；3：交易失败） */
    private Integer tradeStatus;
    /** 交易结果信息 */
    private String resMessage;
    /** 支付截止时间 */
    private LocalDateTime payDeadline;
    /** 支付信息（JSON，采用渠道支付时为渠道支付请求发起时返回的支付信息JSON，当采用线下支付时为审核通过后的线下支付主要信息JSON） */
    private Object paymenInfo;
    /** 是否删除（0：未删除[默认]；1：已删除） */
    private Boolean del;
    /** 创建人ID */
    private Long createBy;
    /** 创建人名称 */
    private String createName;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新人ID */
    private Long updateBy;
    /** 更新人名称 */
    private String updateName;
    /** 更新时间 */
    private LocalDateTime updateTime;

}
