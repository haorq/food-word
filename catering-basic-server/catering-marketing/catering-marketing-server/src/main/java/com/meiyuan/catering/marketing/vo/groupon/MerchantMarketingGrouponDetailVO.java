package com.meiyuan.catering.marketing.vo.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/08 14:08
 * @description APP端团购活动详情VO
 **/

@Data
@ApiModel(value = "APP端团购活动详情VO")
public class MerchantMarketingGrouponDetailVO {

    @ApiModelProperty(value = "基本信息")
    private MarketingGrouponDetailVO baseInfo;
    @ApiModelProperty(value = "商品集合")
    private List<MarketingGrouponGoodsDetailVO> goodsList;
    @ApiModelProperty(value = "活动效果")
    private MarketingGrouponEffectVO effect;

}
