package com.meiyuan.catering.marketing.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/26
 **/
@Data
public class MarketingGoodsSimpleInfoDTO {

    @ApiModelProperty("门店id")
    private String shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
     * 营销活动ID
     */
    @ApiModelProperty("营销活动ID")
    private Long activityId;

    /**
     * 营销活动编号
     */
    @ApiModelProperty("营销活动编号")
    private String activityNo;

    /**
     * 营销活动名称
     */
    @ApiModelProperty("营销活动名称")
    private String activityName;

    @ApiModelProperty("营销商品ID")
    private Long mGoodsId;

    @ApiModelProperty("起购数量")
    private Integer minQuantity;
    /**
     * 限购数量
     */
    @ApiModelProperty(value = "个人-限购数量")
    private Integer limitQuantity;

    @ApiModelProperty(value = "商品市场价")
    private BigDecimal storePrice;

    @ApiModelProperty("活动价格")
    private BigDecimal activityPrice;

    @ApiModelProperty("商品ID")
    private Long goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品SKU")
    private String sku;

    @ApiModelProperty(value = "规格值")
    private String skuValue;

    @ApiModelProperty(value = "V1.5.0 商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;

    @ApiModelProperty("详情图片")
    private String infoPicture;


    @ApiModelProperty(value = "商品标签JSON",hidden = true)
    private String goodsLabel;

    @ApiModelProperty("开始时间")
    private LocalDateTime activityBeginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime activityEndTime;
    @ApiModelProperty(value = "活动上下架：1-下架；2-上架")
    private Integer upDown;

    @ApiModelProperty(value = "商品上下架：1-下架；2-上架")
    private Integer goodsStatus;

    @ApiModelProperty("业务支持：1：仅配送，2：仅自提，3：全部")
    private Integer businessSupport;

    @ApiModelProperty(value = "V1.5.0 商品打包费")
    private BigDecimal packPrice;


}
