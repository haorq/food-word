package com.meiyuan.catering.merchant.dto.pickup;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 自提点绑定解绑接收参数DTO
 * @Date  2020/3/24 0024 16:24
 */
@Data
@ApiModel("自提点绑定解绑接收参数DTO")
public class PickupUpdateRequestDTO {
    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty(value = "自提点id",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pickupPointId;

    @ApiModelProperty(value = "类型:1:绑定，2：解绑",required = true)
    private Integer type;

}
