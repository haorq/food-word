package com.meiyuan.catering.marketing.dto.pickup;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 自提商家信息DTO
 * @Date  2020/3/19 0019 16:29
 */
@Data
@ApiModel("自提店铺信息DTO")
public class PickupGiftShopDTO {
    @ApiModelProperty(value = "id",readOnly = false)
    private Long id;

    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty("商家活动赠品库存")
    private Integer giftQuantity;

}
