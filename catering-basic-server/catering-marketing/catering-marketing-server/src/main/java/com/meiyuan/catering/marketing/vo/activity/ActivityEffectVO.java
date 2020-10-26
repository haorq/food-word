package com.meiyuan.catering.marketing.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/10 14:23
 */

@Data
@ApiModel("活动效果-基本信息")
public class ActivityEffectVO {

    @ApiModelProperty(value = "关联有效订单总数")
    private Long ordersRelationTotal;

    @ApiModelProperty(value = "商品销售额（元）")
    private BigDecimal goodsAmountTotal;

    @ApiModelProperty(value = "平台成本合计（元）")
    private BigDecimal manageAmountTotal;

    @ApiModelProperty(value = "商家成本合计（元）")
    private BigDecimal merchantCostTotal;

    public ActivityEffectVO(){
        this.ordersRelationTotal = 0L;
        this.goodsAmountTotal = BigDecimal.ZERO;
        this.manageAmountTotal = BigDecimal.ZERO;
        this.merchantCostTotal = BigDecimal.ZERO;
    }
}
