package com.meiyuan.catering.marketing.dto.pickup;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 店铺自提赠品库存修改
 * @Date  2020/3/19 0019 16:29
 */
@Data
@ApiModel("店铺自提赠品库存修改")
public class GiftUpdateStockDTO {

    @ApiModelProperty("自提点活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pickupId;

    @ApiModelProperty("赠品赠送数量")
    private Integer number;
}
