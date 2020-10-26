package com.meiyuan.catering.es.dto.marketing;

import com.meiyuan.catering.es.dto.merchant.EsMerchantBaseInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author wxf
 * @date 2020/5/26 11:12
 * @description 简单描述
 **/
@Data
@ApiModel("ES活动DTO")
public class EsMarketingIndexDTO extends EsMerchantBaseInfoDTO {
    /**
     * 活动商品表主键ID--活动下唯一
     * ES中作为ID
     */
    @ApiModelProperty("ES中唯一ID")
    private String mGoodsId;
    /**
     * 活动id 非唯一 ES中不作为ID
     */
    @ApiModelProperty("活动id")
    private String id;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 活动开始时间
     */
    @ApiModelProperty("活动开始时间")
    private LocalDateTime beginTime;
    /**
     * 活动结束时间
     */
    @ApiModelProperty("活动结束时间")
    private LocalDateTime endTime;
    /**
     * 活动对象：0-全部；1-个人；2-企业
     */
    @ApiModelProperty("活动对象：0-全部；1-个人；2-企业")
    private Integer objectLimit;
    /**
     * 活动类型:1-秒杀；2-拼团；3-团购；4-优惠券；
     */
    @ApiModelProperty("活动类型:1-秒杀；2-拼团；3-团购；4-优惠券；")
    private Integer ofType;
    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private String goodsId;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;
    /**
     * 商品市场价
     */
    @ApiModelProperty("商品市场价")
    private BigDecimal storePrice;
    /**
     * 商品图片-列表图
     */
    @ApiModelProperty("商品图片-列表图")
    private String goodsPicture;
    /**
     * 商品sku
     */
    @ApiModelProperty("商品sku")
    private String sku;
    /**
     * 商品数量/发行数量--库存
     */
    @ApiModelProperty("商品数量/发行数量--库存")
    private Integer quantity;
    /**
     * 限购数量
     */
    @ApiModelProperty("限购数量")
    private Integer limitQuantity;
    /**
     * 个人-起购数量
     */
    @ApiModelProperty("个人-起购数量")
    private Integer minQuantity;
    /**
     * 起团数量
     */
    @ApiModelProperty("起团数量")
    private Integer minGrouponQuantity;
    /**
     * 活动价
     */
    @ApiModelProperty("活动价")
    private BigDecimal activityPrice;
    /**
     * 规格值
     */
    @ApiModelProperty("规格值")
    private String skuValue;
    /**
     * 已售数量
     */
    @ApiModelProperty("已售数量")
    private Integer soldOut;
    /**
     * 剩余库存
     */
    @ApiModelProperty("剩余库存")
    private Integer residualInventory;
    /**
     * 上下架状态 1-下架 2-上架
     */
    @ApiModelProperty("上下架状态 1-下架 2-上架")
    private Integer upDownState;
    /**
     * 删除标记
     */
    @ApiModelProperty("删除标记")
    private Boolean del;
    @ApiModelProperty(value = "状态：1-进行中；2-未开始；3-已结束")
    private Integer status;
    @ApiModelProperty("详情图片")
    private String infoPicture;
    @ApiModelProperty("商品详细介绍")
    private String goodsDescribeText;
}
