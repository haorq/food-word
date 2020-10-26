package com.meiyuan.catering.marketing.vo.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/07 14:08
 * @description 团购活动效果-整体活动数据VO
 **/

@Data
@ApiModel(value = "团购活动效果-整体活动数据VO")
public class MarketingGrouponEffectVO {

    @ApiModelProperty(value = "预计成本")
    private BigDecimal projectedCost;
    @ApiModelProperty(value = "实际成本")
    private BigDecimal realCost;
    @ApiModelProperty(value = "预计拉新")
    private Integer projectedPullNew;
    @ApiModelProperty(value = "实际拉新")
    private Integer realPullNew;
    @ApiModelProperty(value = "预计增长营业额")
    private BigDecimal projectedBusiness;
    @ApiModelProperty(value = "实际增长营业额")
    private BigDecimal realBusiness;
    @ApiModelProperty(value = "已成团数量")
    private Integer finishGroup;
    @ApiModelProperty(value = "未成团数量")
    private Integer notGroup;
    @ApiModelProperty(value = "参团总人数")
    private Integer totalGroupMember;
    @ApiModelProperty(value = "商品销售额")
    private BigDecimal business;
    @ApiModelProperty(value = "商品列表")
    private List<MarketingGrouponGoodsEffectVO> goodsList;

}
