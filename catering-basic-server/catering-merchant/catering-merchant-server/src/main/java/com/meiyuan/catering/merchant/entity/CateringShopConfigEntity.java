package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 店铺配置表(CateringShopConfig)实体类
 *
 * @author wxf
 * @since 2020-03-10 10:13:13
 */
@Data
@TableName("catering_shop_config")
public class CateringShopConfigEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = 757491583241986087L;
     /**
     * 店铺id
     */
     @TableField(value = "shop_id")
    private Long shopId;
     /**
     * 1-自动 2-不自动
     */
     @TableField(value = "auto_receipt")
    private Integer autoReceipt;
     /**
     * 1-企业 2-个人 3-全部
     */
     @TableField(value = "delivery_object")
    private Integer deliveryObject;
     /**
     * 配送价格
     */
     @TableField(value = "delivery_price")
    private BigDecimal deliveryPrice;
    /**
     * 满单免配送金额
     */
    @TableField(value = "free_delivery_price")
    private BigDecimal freeDeliveryPrice;
    /**
     * 订单起送金额
     */
    @TableField(value = "least_delivery_price")
    private BigDecimal leastDeliveryPrice;
     /**
     * 配送范围
     */
     @TableField(value = "delivery_range")
    private Integer deliveryRange;
     /**
     * 配送范围规则:1-距离 2-直线
     */
     @TableField(value = "delivery_rule")
    private Integer deliveryRule;
     /**
     * 最快送达时间
     */
     @TableField(value = "fastest_delivery_time")
    private Long fastestDeliveryTime;
     /**
     * 1-支持 2-不支持
     */
     @TableField(value = "shop_yourself")
    private Integer shopYourself;
    /**
     * 订单接收过期时间
     */
    @TableField(value = "order_overtime")
    private LocalDateTime orderOvertime;
    /**
     * 业务支持：1：仅配送，2：仅自提，3：全部
     */
    @TableField(value = "business_support")
    private Integer businessSupport;
    /**
     * 0-否 1-是
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    private Boolean del;
    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    }