package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/10/09 10:10
 * @description 订单分账流水表实体类
 **/

@Data
@TableName("catering_orders_split_bill_order_flow")
public class CateringOrdersSplitBillOrderFlowEntity extends IdEntity implements Serializable {

    /**
     * 分账流水表ID
     */
    @TableField("split_bill_id")
    private Long splitBillId;
    /**
     * 系统交易流水编号
     */
    @TableField("trading_number")
    private String tradingNumber;
    /**
     * 第三方交易流水编号
     */
    @TableField("third_trade_number")
    private String thirdTradeNumber;
    /**
     * 收款人
     */
    @TableField("collection_user")
    private String collectionUser;
    /**
     * 订单分账金额
     */
    @TableField("order_split_amount")
    private BigDecimal orderSplitAmount;
    /**
     * 手续费率（单位：%）
     */
    @TableField("fee_rate")
    private BigDecimal feeRate;
    /**
     * 手续费
     */
    @TableField("fee")
    private BigDecimal fee;
    /**
     * 分账类型（1：商家可分订单金额；2：订单配送费；3：商家负债分账）
     */
    @TableField("type")
    private Integer type;
    /**
     * 分账规则
     */
    @TableField("split_rule")
    private String splitRule;

    /**
     * 类型 1:分账 2:内扣(平台分账)
     */
    @TableField("split_type")
    private Integer splitType;


    /**
     * 交易状态（0：未交易；1：交易中；2：交易成功；3：交易失败；4：待交易）
     */
    @TableField("trade_status")
    private Integer tradeStatus;
    /**
     * 失败原因
     */
    @TableField("fail_message")
    private String failMessage;
    /**
     * 成功时间
     */
    @TableField("success_time")
    private LocalDateTime successTime;
    /**
     * 通知参数信息
     */
    @TableField("notify_message")
    private String notifyMessage;
    /**
     * 是否删除（0：未删除[默认]；1：已删除）
     */
    @TableField("is_del")
    @TableLogic
    private Boolean del;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;

    public CateringOrdersSplitBillOrderFlowEntity() {
    }

    public CateringOrdersSplitBillOrderFlowEntity(Long splitBillId, String collectionUser, BigDecimal orderSplitAmount, Integer type, Integer splitType) {
        this.splitBillId = splitBillId;
        this.collectionUser = collectionUser;
        this.orderSplitAmount = orderSplitAmount;
        this.splitType = splitType;
        this.type = type;
    }

    public CateringOrdersSplitBillOrderFlowEntity(Long splitBillId, Long collectionUser, BigDecimal orderSplitAmount, Integer type, Integer splitType) {
        this.splitBillId = splitBillId;
        this.collectionUser = collectionUser.toString();
        this.orderSplitAmount = orderSplitAmount;
        this.splitType = splitType;
        this.type = type;
    }
}
