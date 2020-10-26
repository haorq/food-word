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
 * @description 补贴分账流水表实体类
 **/

@Data
@TableName("catering_orders_split_bill_subsidy_flow")
public class CateringOrdersSplitBillSubsidyFlowEntity extends IdEntity implements Serializable {

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
     * 订单补贴金额
     */
    @TableField("order_subsidy_amount")
    private BigDecimal orderSubsidyAmount;
    /**
     * 补贴类型（1：优惠券；2：配送）
     */
    @TableField("type")
    private Integer type;
    /**
     * 交易状态（0：未交易；1：交易中；2：交易成功；3：交易失败；4：待交易；5：交易关闭）
     */
    @TableField("trade_status")
    private Integer tradeStatus;
    /**
     * 失败原因，关闭原因
     */
    @TableField("fail_message")
    private String failMessage;
    /**
     * 成功时间
     */
    @TableField("success_time")
    private LocalDateTime successTime;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
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
