package com.meiyuan.catering.order.dto.submit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单创建信息Dto
 *
 * @Author XiJie-Xie
 * @email 1121075903@qq.com
 * @create 2020/3/10 13:41
 */
@Data
public class OrdersCreateDTO implements Serializable {

    private static final long serialVersionUID = -2187562448067887804L;
    /** 主键ID  */
    private Long id;
    /** 订单编号 */
    private String orderNumber;
    /** 商户ID */
    private Long merchantId;
    /** 商户电话 */
    private String merchantPhone;
    /** 门店ID */
    private Long storeId;
    /** 门店名称 */
    private String storeName;
    /** 门店图片 */
    private String storePicture;
    /** 下单客户ID */
    private Long memberId;
    /** 下单客户头像 */
    private String avatar;
    /** 下单客户编号 */
    private String memberNo;
    /** 下单客户电话 */
    private String memberPhone;
    /** 下单客户名称 */
    private String memberName;
    /** 下单客户属性（1：企业用户；2：个人用户） */
    private Integer memberType;
    /** 开单时间 */
    private LocalDateTime billingTime;
    /** 下单方式（1：外卖，2：堂食） */
    private Integer orderWay;
    /** 订单来源（1：外卖小程序，2：堂食小程序） */
    private Integer orderSource;
    /** 订单类型（1：普通订单，2：团购订单，3：拼单订单） */
    private Integer orderType;
    /** 取餐方式（1:外卖配送，2:到店自取，3:堂食正餐，4:堂食快餐，5:堂食外带） */
    private Integer deliveryWay;
    /** 订单状态（1：代付款；2：待接单；3：待配送；4：配送中；5：待取餐；6：已完成；7：已取消） */
    private Integer status;
    /** 商品总金额 */
    private BigDecimal goodsAmount;
    /** 配送费 */
    private BigDecimal deliveryPrice;

    /** 配送费初始价格 */
    private BigDecimal deliveryPriceOriginal;

    /** 配送费满减标准 **/
    private BigDecimal deliveryPriceFree;



    /** 包装费 */
    private BigDecimal packPrice;
    /** 优惠前订单金额 */
    private BigDecimal discountBeforeFee;
    /** 优惠后订单金额 */
    private BigDecimal discountLaterFee;
    /** 优惠金额 */
    private BigDecimal discountFee;
    /** 订单总金额 */
    private BigDecimal orderAmount;
    /** 已支付金额 */
    private BigDecimal paidAmount;
    /** 订单商品总件数 */
    private Integer totalQuantity;
    /** 支付截止时间（订单自动关闭时间） */
    private LocalDateTime payDeadline;
    /** 订单备注 */
    private String remarks;
    /** 卡路里 */
    private BigDecimal calories;
    /** 是否能支付（1：能支付[默认]； 2：不能支付（商品信息被修改）） */
    private Boolean canPay;
    /** 是否已评论（0：否；1：是）默认为0 */
    private Boolean comment;
    /** 订单是否删除（0：未删除[默认]；1：已删除） */
    private Boolean del;
    /** 订单配送信息 */
    private OrdersDeliveryCreateDTO delivery;
    /** 订单商品列表 */
    private List<OrdersGoodsCreateDTO> goodsList;
    /** 订单优惠列表 */
    private List<OrdersDiscountsCreateDTO> discountsList;
    /** 订单活动列表 */
    private List<OrdersActivityCreateDTO> activityList;
    /** 配送ID */
    private Long deliveryId;
    /**
     * 门店配送方式标识（0：自配送[默认]；1：达达配送）
     */
    private Integer shopDeliveryFlag;
}
