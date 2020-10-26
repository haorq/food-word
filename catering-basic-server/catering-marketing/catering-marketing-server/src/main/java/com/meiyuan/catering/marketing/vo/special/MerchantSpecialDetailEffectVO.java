package com.meiyuan.catering.marketing.vo.special;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/03 20:09
 * @description 商户APP端营销特价商品详情效果数据信息VO
 **/

@Data
@ApiModel(value = "商户APP端营销特价商品详情效果数据信息VO")
public class MerchantSpecialDetailEffectVO {

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

}
