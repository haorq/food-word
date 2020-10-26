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
 * @date 2020/10/13 11:10
 * @description 商家负债明细表实体类
 **/

@Data
@TableName("catering_orders_shop_debt_flow")
public class CateringOrdersShopDebtFlowEntity extends IdEntity implements Serializable {

    /**
     * 负债表ID
     */
    @TableField("shop_debt_id")
    private Long shopDebtId;
    /**
     * 商户ID
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 负债金额
     */
    @TableField("debt_amount")
    private BigDecimal debtAmount;
    /**
     * 平台补贴金额
     */
    @TableField("subsidy_amount")
    private BigDecimal subsidyAmount;
    /**
     * 商家应付/已付金额
     */
    @TableField("amount")
    private BigDecimal amount;
    /**
     * 负债类型（1：配送费）
     */
    @TableField("debt_type")
    private Integer debtType;
    /**
     * 款项类型（1：应付；2：已付）
     */
    @TableField("amount_type")
    private Integer amountType;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 是否删除（0：否；1：是）
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
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

}
