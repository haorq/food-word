package com.meiyuan.catering.marketing.dto.pickup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mt
 * @date 2020/3/16
 **/
@Data
@ApiModel("自提活动赠品剩余DTO")
public class PickupRemainGiftDTO {

    @ApiModelProperty("赠品ID")
    private Long id;

    @ApiModelProperty("赠品剩余数量")
    private Integer remainGiftQuantity;

}
