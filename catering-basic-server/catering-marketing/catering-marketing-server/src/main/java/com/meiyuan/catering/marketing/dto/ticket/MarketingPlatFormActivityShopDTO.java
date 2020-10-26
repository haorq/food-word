package com.meiyuan.catering.marketing.dto.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MarketingPlatFormActivityShopDTO
 * @Description
 * @Author gz
 * @Date 2020/8/8 17:16
 * @Version 1.3.0
 */
@Data
public class MarketingPlatFormActivityShopDTO {
    @ApiModelProperty(value = "平台活动id")
    private Long activityId;
    @ApiModelProperty(value = "参与门店id集合")
    private List<Long> shopList;
    @ApiModelProperty(value = "商户id",hidden = true)
    private Long merchantId;
}
