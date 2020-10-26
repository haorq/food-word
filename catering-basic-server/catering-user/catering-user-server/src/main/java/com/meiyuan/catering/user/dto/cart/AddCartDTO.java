package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/7/2 14:38
 * @since v1.2.0
 */
@Data
@ApiModel("添加购物车属性")
public class AddCartDTO implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "购物车ID", hidden = true)
    private Long id;
    @ApiModelProperty(value = "用户ID/企业用户ID", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer userType;

    @ApiModelProperty(value = "菜品分类id", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;

    @ApiModelProperty(value = "价格", hidden = true)
    private BigDecimal price;
    @ApiModelProperty(value = "下架：1；上架：2 ", hidden = true)
    private Integer goodsStatus;


    @ApiModelProperty(value = "商户ID", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    /**
     * @since v1.2.0
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;
    @ApiModelProperty(value = "商品ID/活动商品ID（秒杀）", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty(value = "商品类型 1:普通商品 2:秒杀商品 4:特价商品", required = true)
    private Integer goodsType;
    @ApiModelProperty(value = "商品sku编码", required = true)
    private String skuCode;


    @ApiModelProperty(value = "商品名称", required = true)
    private String goodsName;
    @ApiModelProperty(value = "商品sku名称", required = true)
    private String skuName;
    @ApiModelProperty(value = "商品货品的数量", required = true)
    private Integer number;
    @ApiModelProperty(value = "购物车类型  1:普通 2:拼单", required = true)
    private Integer type;

    @ApiModelProperty("拼单号(拼单传)")
    private String shareBillNo;
    @ApiModelProperty(value = "拼单创建人id", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shareUserId;

    /**
     * 描述:区分秒杀专场  , 购物车  添加秒杀商品
     *
     * @since v1.2.0
     */
    @ApiModelProperty(value = "是否是购物车请求(秒杀商品传)", required = true)
    private Boolean cartRequest;

    /**
     * 描述:秒杀商品必传
     *
     * @since v1.3.0
     * @since v1.4.0 goodsType = 2 秒杀场次Id ,goodsType = 4 特价活动id
     */
    @ApiModelProperty(value = "goodsType = 2 秒杀场次Id ,goodsType = 4 特价活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillEventId;

    /**
     * 描述: 每单限多少份优惠使用
     *
     * @since v1.2.0
     */
    @ApiModelProperty(value = "商品已购的数量(普通商品)", required = true)
    private Integer haveNumber;
    /**
     * 描述: 每单限多少份优惠使用
     *
     * @since v1.2.0
     */
    @ApiModelProperty(value = "每单限x份优惠", hidden = true)
    private Integer discountLimit;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "原价", hidden = true)
    private BigDecimal marketPrice;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "现价", hidden = true)
    private BigDecimal salesPrice;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "企业价", hidden = true)
    private BigDecimal enterprisePrice;
    /**
     * @since v1.5.0
     */
    @ApiModelProperty(value = "包装费", hidden = true)
    private BigDecimal packPrice;

    /**
     * 总价
     *
     * @since v1.2.0
     */
    @ApiModelProperty(value = "单次添加商品的总价(number*对应价格)", hidden = true)
    private BigDecimal totalPrice;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "商品规格类型 1-统一规格 2-多规格", hidden = true)
    private Integer goodsSpecType;


    public AddCartDTO() {
        this.cartRequest = false;
        this.haveNumber = 0;
        this.goodsSpecType = 1;
    }


}
