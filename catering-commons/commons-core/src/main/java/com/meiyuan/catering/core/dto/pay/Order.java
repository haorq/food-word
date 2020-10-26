package com.meiyuan.catering.core.dto.pay;

import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/5/19 16:54
 * @since v1.1.0
 */
@Data
public class Order extends IdEntity {
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
     * 门店ID
     */
    private Long storeId;
    /**
     * 下单客户ID
     */
    private Long memberId;
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
     * 订单来源（1：小程序）
     */
    private Integer orderSource;
    /**
     * 订单类型（1：普通订单，2：团购订单，3：拼单订单，4：菜单订单）
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
     * 支付截止时间
     */
    private LocalDateTime payDeadline;
    /**
     * 支付方式（ 1：余额支付；2：微信支付（WX)； 3：支付宝支付(ALP)；4：POS刷卡支付(YH)；）（为用户在订单完成时最后选择的支付方式）
     */
    private Integer payWay;

    /**
     * 是否能支付（1：能支付[默认]； 2：不能支付（商品信息被修改））
     */
    private Boolean canPay;
    /**
     * 是否已评论（0：否；1：是）默认为0
     */
    private Boolean comment;
    /**
     * 是否可以申请售后（0：否[默认]； 1：是）
     */
    private Boolean canAfterSales;
    /**
     * 是否申请售后（0：不能[默认]； 1：能）
     */
    private Boolean afterSales;
    private String updateName;
}
