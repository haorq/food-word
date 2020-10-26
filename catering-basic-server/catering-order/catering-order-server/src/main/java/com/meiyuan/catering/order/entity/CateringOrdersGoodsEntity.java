package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单商品表(CateringOrdersGoods)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@TableName("catering_orders_goods")
public class CateringOrdersGoodsEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = -85820195345161452L;

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
     * 商品类型（1--普通商品，2--秒杀商品；3--团购商品；）
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
    @TableField("is_gifts")
    private Boolean gifts;
    /**
     * 赠品活动id
     */
    private Long giftsActivityId;
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
     * 是否删除（0：未删除[默认]；1：已删除）
     */
    @TableField("is_del")
    @TableLogic
    private Boolean del;
    /**
     * 创建人ID
     */
    private Long createBy;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新人ID
     */
    private Long updateBy;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 每单限x份优惠
     */
    private Integer discountLimit;
    /**
     * 秒杀场次Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skillEventId;
    /**
     * 餐盒费
     */
    private BigDecimal packPrice;

}
