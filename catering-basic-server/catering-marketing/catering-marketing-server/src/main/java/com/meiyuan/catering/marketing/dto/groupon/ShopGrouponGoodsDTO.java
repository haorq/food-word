package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName ActivityGoodsFilterDTO
 * @Description
 * @Author gz
 * @Date 2020/3/24 18:21
 * @Version 1.1
 */
@Data
public class ShopGrouponGoodsDTO {
    @ApiModelProperty("店铺id")
    private Long shopId;
    @ApiModelProperty("店铺中活动最低价格")
    private BigDecimal activityPrice;
}
