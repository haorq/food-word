package com.meiyuan.catering.marketing.vo.groupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/08/07 14:08
 * @description 团购活动效果-活动商品数据VO
 **/

@Data
@ApiModel(value = "团购活动效果-活动商品数据VO")
public class MarketingGrouponGoodsEffectVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("商品ID")
    private Long goodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品规格编码")
    private String skuCode;
    @ApiModelProperty("商品规格值")
    private String propertyValue;
    @ApiModelProperty("规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("活动价格")
    private BigDecimal activityPrice;
    @ApiModelProperty("起团总数量")
    private Integer totalGrouponNum;
    @ApiModelProperty("当前团购总数量")
    private Integer totalGrouponBuyNum;
    @ApiModelProperty("参团人数")
    private Integer grouponMember;
    @ApiModelProperty("成团状态 true-已成团 false-未成团")
    private Boolean grouponStatus;
    @ApiModelProperty(value = "商品销售额")
    private BigDecimal business;

    /**
     * 营销商品ID
     */
    @JsonIgnore
    private Long mGoodsId;

}
