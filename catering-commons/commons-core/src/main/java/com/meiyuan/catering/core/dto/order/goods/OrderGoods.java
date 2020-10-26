package com.meiyuan.catering.core.dto.order.goods;

import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/5/20 11:08
 * @since v1.1.0
 */
@Data
public class OrderGoods extends IdEntity {
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 商品编号
     */
    private String goodsNumber;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品类型（1：普通商品；2：菜单；3：拼单；4：秒杀；5：团购；6—菜单拼单）
     */
    private Integer goodsType;
    /**
     * 商品SPU编码
     */
    private String goodsSpuCode;
    /**
     * 商品SKU编码
     */
    private String goodsSkuCode;
    /**
     * 商品购买数量
     */
    private Integer quantity;
    /**
     * 市场单价
     */
    private BigDecimal storePrice;
    /**
     * 销售单价
     */
    private BigDecimal salesPrice;
    /**
     * 成交单价
     */
    private BigDecimal transactionPrice;
    /**
     * 配送费
     */
    private BigDecimal deliveryPrice;
    /**
     * 打包费
     */
    private BigDecimal packPrice;
    /**
     * 优惠前商品金额
     */
    private BigDecimal discountBeforeFee;
    /**
     * 优惠后商品金额
     */
    private BigDecimal discountLaterFee;
    /**
     * 优惠金额
     */
    private BigDecimal discountFee;
    /**
     * 商品分类ID
     */
    private Long goodsGroupId;
    /**
     * 商品分类名称
     */
    private String goodsGroupName;
    /**
     * 商品规格描述
     */
    private String goodsSpecificationDesc;
    /**
     * 商品标签
     */
    private String goodsLabelName;
    /**
     * 商品图片
     */
    private String goodsPicture;
    /**
     * 是否赠品（0：不是赠品[默认]；1：是赠品）
     */
    private Boolean gifts;
    /**
     * 赠品活动id
     */
    private Long giftsActivityId;
    /**
     * 是否使用积分抵扣（0：没有使用积分；1：使用积分）
     */
    private Boolean usedScore;
    /**
     * 本次消耗积分
     */
    private Integer consumeScore;
    /**
     * 本次积分抵扣金额
     */
    private BigDecimal consumeScoreFee;
    /**
     * 是否子商品（0：不是[默认]；1：是。此处主要用于表示套餐下属商品信息）
     */
    private Boolean childGoods;
    /**
     * 父商品ID（当is_child_goods=true时，此字段有效，表示上级order_goods表ID）
     */
    private Long orderGoodsParentId;
    /**
     * 拼单用户id
     */
    private Long shareBillUserId;
    /**
     * 拼单用户名称
     */
    private String shareBillUserName;
    /**
     * 拼单用户头像
     */
    private String shareBillUserAvatar;
    /**
     * 菜单ID
     */
    private Long menuId;
    /**
     * 菜单名称
     */
    private String menuName;
}
