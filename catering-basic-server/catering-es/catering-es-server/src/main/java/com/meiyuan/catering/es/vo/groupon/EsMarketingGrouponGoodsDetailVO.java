package com.meiyuan.catering.es.vo.groupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BaseUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/08/08 13:08
 * @description 团购商品详情VO
 **/

@Data
@ApiModel(value = "团购商品详情VO")
public class EsMarketingGrouponGoodsDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "营销商品ID")
    private Long mGoodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品详情图片")
    private String infoPicture;
    @ApiModelProperty(value = "商品主团")
    private String listPicture;
    @ApiModelProperty(value = "起团数量(多少份起团)")
    private Integer minGroupQuantity;
    @ApiModelProperty(value = "售出数量(已团多少份)")
    private Integer soldOut;
    @ApiModelProperty(value = "原价")
    private BigDecimal storePrice;
    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "商家ID")
    private Long shopId;
    @ApiModelProperty(value = "商家Logo")
    private String doorHeadPicture;
    @ApiModelProperty(value = "商家名称")
    private String shopName;
    @ApiModelProperty(value = "商家业务支持  1：仅配送，2：仅自提，3：全部")
    private Integer businessSupport;
    @ApiModelProperty(value = "V1.4.0 商品简介")
    private String goodsSynopsis;
    @ApiModelProperty("商品详细介绍")
    private String goodsDescribeText;
    @ApiModelProperty(value = "商品SKU编码")
    private String sku;
    @ApiModelProperty(value = "商品SKU值")
    private String skuValue;
    @ApiModelProperty(value = "规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "团购活动ID")
    private Long grouponId;
    @ApiModelProperty(value = "起购数量")
    private Integer minQuantity;
    @ApiModelProperty(value = "团购状态 1：未开始 2：进行中 3：已结束")
    private Integer status;
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "属性:1:自营，2：非自营")
    private Integer merchantAttribute;
    @ApiModelProperty(value = "餐盒费")
    private BigDecimal packPrice;

    public String getListPicture() {
        return BaseUtil.subFirstByComma(infoPicture);
    }
}
