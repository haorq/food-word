package com.meiyuan.catering.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

/**
 * 订单优惠信息表(CateringOrdersDiscounts)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@TableName("catering_orders_discounts")
public class CateringOrdersDiscountsEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = -16866394751612074L;

    /** 商户ID */
    private Long merchantId;
    /** 订单ID */
    private Long orderId;
    /** 订单编号 */
    private String orderNumber;
    /** 优惠类型（1：优惠券；2：促销；3：会员卡；4：积分） */
    private Integer discountType;
    /** 优惠源ID（根据类型不同区分） */
    private Long discountId;
    /** 优惠源编号 */
    private String discountNumber;
    /** 优惠源名称 */
    private String discountName;
    /** 优惠折扣比例（折扣详细数值，如85折为0.85） */
    private BigDecimal discountRate;
    /** 优惠使用积分 */
    private Integer discountScore;
    /** 优惠金额 */
    private BigDecimal discountAmount;
    /** 使用条件：1：订单优惠；2：商品优惠 */
    private Integer usefulCondition;
    /** 优惠描述 */
    private String discountDescribe;
    /** 数量 */
    private Integer discountTotal;
    /** 备注 */
    private String remarks;
    /** 是否删除（0：未删除[默认]；1：已删除） */
    @TableField("is_del")
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
