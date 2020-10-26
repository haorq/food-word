package com.meiyuan.catering.marketing.vo.grouponorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/11 16:08
 * @description 团购营销活动商品的已团份数情况VO
 **/

@Data
@ApiModel(value = "团购营销活动商品的已团份数情况VO")
public class MarketingGrouponOrderSoldVo {

    @ApiModelProperty(value = "营销商品ID")
    private Long mGoodsId;
    @ApiModelProperty(value = "已团份数")
    private Integer sold;

}
