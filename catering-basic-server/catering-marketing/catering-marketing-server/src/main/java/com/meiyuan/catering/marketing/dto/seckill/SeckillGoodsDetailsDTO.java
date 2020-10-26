package com.meiyuan.catering.marketing.dto.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName SeckillGoodsDetailsDTO
 * @Description 秒杀商品详情DTO
 * @Author gz
 * @Date 2020/3/26 10:44
 * @Version 1.1
 */
@Data
public class SeckillGoodsDetailsDTO implements Serializable {
    private static final long serialVersionUID = 5359495785405609183L;
    @ApiModelProperty(value = "活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动对象：0-全部；1-个人；2-企业")
    private Integer objectLimit;
    @ApiModelProperty(value = "商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    /**
     * 商品表主键ID--活动下唯一
     */
    @ApiModelProperty(value = "商品表主键ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long mGoodsId;

    @ApiModelProperty(value = "商品上下架状态")
    private Integer goodsStatus;
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品市场价")
    private BigDecimal storePrice;

    @ApiModelProperty(value = "商品图片-列表图")
    private String goodsPicture;
    /**
     * 商品sku
     */
    @ApiModelProperty(value = "商品sku")
    private String sku;
    /**
     * 商品数量/发行数量--库存
     */
    @ApiModelProperty(value = "商品数量/发行数量")
    private Integer quantity;
    /**
     * 限购数量
     */
    @ApiModelProperty(value = "个人-限购数量")
    private Integer limitQuantity;
    @ApiModelProperty(value = "个人-起购数量")
    private Integer minQuantity;
    /**
     * 起团数量
     */
    @ApiModelProperty(value = "起团数量")
    private Integer minGrouponQuantity;
    /**
     * 活动价
     */
    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;

    @ApiModelProperty(value = "规格值")
    private String skuValue;
    @ApiModelProperty(value = "已售数量")
    private Integer soldOut;
    @ApiModelProperty(value = "剩余库存")
    private Integer residualInventory;
    @ApiModelProperty(value = "上下架状态")
    private Integer upDownState;
    @ApiModelProperty(value = "删除标记")
    private Boolean del;
}
