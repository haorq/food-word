package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BaseUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yaoozu
 * @description 购物车商品
 * @date 2020/3/2516:51
 * @since v1.0.0
 */
@Data
@ApiModel("购物车商品DTO")
public class CartGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1431461236844526678L;
    @ApiModelProperty("购物车ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @ApiModelProperty("商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("菜品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("名称")
    private String goodsName;
    @ApiModelProperty("列表图片")
    private String listPicture;
    @ApiModelProperty("详情图片")
    private String infoPicture;
    @ApiModelProperty("现价 直接用price")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价 直接用price")
    private BigDecimal enterprisePrice;
    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("单价 购物车价格")
    private BigDecimal price;
    @ApiModelProperty("包装费")
    private BigDecimal packPrice;

    @ApiModelProperty("商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("编码")
    private String skuCode;
    @ApiModelProperty("规格值")
    private String propertyValue;

    @ApiModelProperty("数量")
    private Integer number;
    @ApiModelProperty("购物车类型:1--普通，2--拼单")
    private Integer type;
    @ApiModelProperty("商品类型:1--普通商品，2--秒杀")
    private Integer goodsType;
    @ApiModelProperty(value = "秒杀场次ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillEventId;
    @ApiModelProperty("最低购买")
    private Integer lowestBuy;
    @ApiModelProperty("最多购买 -1无限制")
    private Integer highestBuy;

    @ApiModelProperty("商品信息变动标识 true--有变动，false--无变动")
    private Boolean changeFlag;

    @ApiModelProperty("总价")
    private BigDecimal totalPrice;

    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "剩余库存")
    private Integer residualInventory;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "每单限x份优惠")
    private Integer discountLimit;

    public String getListPicture() {

        if (StringUtils.isEmpty(infoPicture)){
            return "";
        }

        if (infoPicture.contains(BaseUtil.COMMA)) {
            return infoPicture.split(BaseUtil.COMMA)[0];
        }
        return infoPicture;
    }
}
