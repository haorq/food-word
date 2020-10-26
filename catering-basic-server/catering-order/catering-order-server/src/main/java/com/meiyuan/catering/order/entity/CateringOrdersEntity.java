package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表(CateringOrders)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@TableName("catering_orders")
public class CateringOrdersEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = -24062012706120078L;

    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 系统交易流水编号
     */
    private String tradingFlow;
    /**
     * 商户ID
     */
    private Long merchantId;
    /**
     * 商家电话
     */
    private String merchantPhone;
    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 门店图片
     */
    private String storePicture;
    /**
     * 下单客户ID
     */
    private Long memberId;
    /**
     * 下单客户编号
     */
    private String memberNo;
    /**
     * 下单客户头像
     */
    private String avatar;
    /**
     * 下单客户电话
     */
    private String memberPhone;
    /**
     * 下单客户名称
     */
    private String memberName;
    /**
     * 下单客户属性（1：企业用户；2：个人用户）
     */
    private Integer memberType;
    /**
     * 开单时间
     */
    private LocalDateTime billingTime;
    /**
     * 下单方式（1:外卖小程序，2:堂食小程序）
     */
    private Integer orderSource;
    /**
     * 下单类型（1:外卖，2:堂食）
     */
    private Integer orderWay;
    /**
     * 订单类型（1：普通订单，2：团购订单，3：拼单订单）
     */
    private Integer orderType;
    /**
     * 取餐方式（1：外卖配送，2：到店自取）
     */
    private Integer deliveryWay;
    /**
     * 订单状态（1：代付款；2：待接单；3：待配送；4：待取餐；5：已完成；6：已取消；7：已关闭；8：团购中）
     */
    private Integer status;
    /**
     * 商品总金额
     */
    private BigDecimal goodsAmount;
    /**
     * 配送费
     */
    private BigDecimal deliveryPrice;
    /**
     * 配送费初始价格
     */
    private BigDecimal deliveryPriceOriginal;
    /**
     * 配送费满减标准
     */
    private BigDecimal deliveryPriceFree;
    /**
     * 包装费
     */
    private BigDecimal packPrice;
    /**
     * 优惠前订单金额
     */
    private BigDecimal discountBeforeFee;
    /**
     * 优惠后订单金额
     */
    private BigDecimal discountLaterFee;
    /**
     * 优惠金额
     */
    private BigDecimal discountFee;
    /**
     * 订单总金额
     */
    private BigDecimal orderAmount;
    /**
     * 已支付金额
     */
    private BigDecimal paidAmount;
    /**
     * 支付完成时间
     */
    private LocalDateTime paidTime;
    /**
     * 支付方式（ 1：余额支付；2：微信支付（WX)； 3：支付宝支付(ALP)；4：POS刷卡支付(YH)；）（为用户在订单完成时最后选择的支付方式）
     */
    private Integer payWay;
    /**
     * 订单商品总件数
     */
    private Integer totalQuantity;
    /**
     * 支付截止时间（订单自动关闭时间）
     */
    private LocalDateTime payDeadline;
    /**
     * 订单备注
     */
    private String remarks;
    /**
     * 订单关闭取消原因
     */
    private String offReason;
    /**
     * 卡路里
     */
    private BigDecimal calories;
    /**
     * 是否能支付（1：能支付[默认]； 2：不能支付（商品信息被修改））
     */
    @TableField("is_can_pay")
    private Boolean canPay;
    /**
     * 是否已评论（0：否；1：是）默认为0
     */
    @TableField("is_comment")
    private Boolean comment;
    /**
     * 是否可以申请售后（0：否[默认]； 1：是）
     */
    @TableField("is_can_after_sales")
    private Boolean canAfterSales;
    /**
     * 是否申请售后（0：不能[默认]； 1：能）
     */
    @TableField("is_after_sales")
    private Boolean afterSales;
    /**
     * 门店配送方式标识（0：自配送[默认]；1：达达配送）
     */
    @TableField("shop_delivery_flag")
    private Integer shopDeliveryFlag;
    /**
     * 订单是否删除（0：未删除[默认]；1：已删除）
     */
    @TableField("is_del")
    private Boolean del;
    /**
     * 订单创建人ID
     */
    private Long createBy;
    /**
     * 订单创建人名称
     */
    private String createName;
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
    /**
     * 订单更新人ID
     */
    private Long updateBy;
    /**
     * 订单更新人名称
     */
    private String updateName;
    /**
     * 订单更新时间
     */
    private LocalDateTime updateTime;

}
