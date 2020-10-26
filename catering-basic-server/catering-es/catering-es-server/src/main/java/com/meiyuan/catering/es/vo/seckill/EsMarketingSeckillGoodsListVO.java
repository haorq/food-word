package com.meiyuan.catering.es.vo.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/08/08 10:08
 * @description 小程序限时秒杀指定场次商品列表VO
 **/

@Data
@ApiModel(value = "小程序限时秒杀指定场次商品列表VO")
public class EsMarketingSeckillGoodsListVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "营销商品ID")
    private Long mGoodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "发行数量")
    private Integer quantity;
    @ApiModelProperty(value = "商品图片")
    private String goodsPicture;
    @ApiModelProperty(value = "用户与商品之间的距离（单位：米）")
    private Double distance;
    @ApiModelProperty(value = "售出数量")
    private Integer soldOut;
    @ApiModelProperty(value = "原价")
    private BigDecimal storePrice;
    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "商家ID")
    private Long shopId;
    @ApiModelProperty(value = "商家名称")
    private String shopName;
    @ApiModelProperty(value = "商家图片")
    private String doorHeadPicture;
    @ApiModelProperty(value = "商家业务支持  1：仅配送，2：仅自提，3：全部")
    private Integer businessSupport;
    @ApiModelProperty(value = "商品SKU编码")
    private String sku;
    @ApiModelProperty(value = "商品SKU值")
    private String skuValue;
    @ApiModelProperty(value = "规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "秒杀活动ID")
    private Long seckillId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "秒杀场次ID")
    private Long eventId;
    @ApiModelProperty(value = "起购数量")
    private Integer minQuantity;
    @ApiModelProperty(value = "限购数量")
    private Integer limitQuantity;
    @ApiModelProperty(value = "餐盒费")
    private BigDecimal packPrice;

}
