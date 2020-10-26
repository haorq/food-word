package com.meiyuan.catering.merchant.dto.pickup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.merchant.dto.gift.ShopGiftGoodResponseDTO;
import com.meiyuan.catering.merchant.dto.shop.config.TimeRangeResponseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @Author MeiTao
 * @Description 店铺自提配置信息
 * @Date  2020/3/22 0022 18:15
 */
@Data
@ApiModel("店铺自提配置信息")
public class PickupConfigResponseDTO {
    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "店铺赠品信息")
    private List<ShopGiftGoodResponseDTO> giftGoods;

    @ApiModelProperty(value = "自提时间范围")
    private List<TimeRangeResponseDTO> pickupTimes;

}
