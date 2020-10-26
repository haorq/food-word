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
 * @description 分账表实体类
 **/

@Data
@TableName("catering_orders_split_bill")
public class CateringOrdersSplitBillEntity extends IdEntity implements Serializable {

    /**
     * 系统交易编号
     */
    @TableField("trading_flow")
    private String tradingFlow;
    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 订单编号
     */
    @TableField("order_number")
    private String orderNumber;
    /**
     * 付款人
     */
    @TableField("pay_user")
    private String payUser;
    /**
     * 分账金额
     */
    @TableField("split_amount")
    private BigDecimal splitAmount;
    /**
     * 订单金额
     */
    @TableField("order_amount")
    private BigDecimal orderAmount;
    /**
     * 补贴金额
     */
    @TableField("subsidy_amount")
    private BigDecimal subsidyAmount;
    /**
     * 业务码
     */
    @TableField("trade_code")
    private String tradeCode;
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
     * 请求参数信息
     */
    @TableField("request_message")
    private String requestMessage;
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

}
