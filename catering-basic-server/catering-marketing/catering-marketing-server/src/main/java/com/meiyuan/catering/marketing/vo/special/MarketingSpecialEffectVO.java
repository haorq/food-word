package com.meiyuan.catering.marketing.vo.special;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/03 17:09
 * @description 营销特价商品活动效果信息VO
 **/

@Data
@ApiModel(value = "营销特价商品活动效果信息VO")
public class MarketingSpecialEffectVO {

    @ApiModelProperty(value = "实际成本")
    private BigDecimal realCost;
    @ApiModelProperty(value = "商品销售额")
    private BigDecimal business;
    @ApiModelProperty(value = "预计拉新")
    private Integer projectedPullNew;
    @ApiModelProperty(value = "实际拉新")
    private Integer realPullNew;
    @ApiModelProperty(value = "预计增长营业额")
    private BigDecimal projectedBusiness;
    @ApiModelProperty(value = "实际增长营业额")
    private BigDecimal realBusiness;
    @ApiModelProperty(value = "定价方式 1-统一折扣 2-折扣 3-固定价")
    private Integer fixType;

}
