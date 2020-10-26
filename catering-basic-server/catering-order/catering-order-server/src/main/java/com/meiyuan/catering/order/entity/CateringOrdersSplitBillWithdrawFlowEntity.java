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
 * @description 分账提现流水表实体类
 **/

@Data
@TableName("catering_orders_split_bill_withdraw_flow")
public class CateringOrdersSplitBillWithdrawFlowEntity extends IdEntity implements Serializable {

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
     * 收款账号
     */
    @TableField("collection_account")
    private String collectionAccount;
    /**
     * 收款账号属性: 0-个人银行卡、1-企业对公账户
     */
    @TableField("collection_account_pro")
    private Long collectionAccountPro;
    /**
     * 提现金额
     */
    @TableField("withdraw_amount")
    private BigDecimal withdrawAmount;
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
     * 提现方式（D0:D+0到账  D1:D+1到账  T1customized:T+1到账，仅工作日代付  D0customized:D+0到账，根据平台资金头寸付款）
     */
    @TableField("type")
    private String type;
    /**
     * 交易状态（0：未交易；1：交易中；2：交易成功；3：交易失败）
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

}
