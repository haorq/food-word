package com.meiyuan.catering.marketing.dto.pickup;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Description 自提赠送活动添加DTO
 * @Date  2020/3/19 0019 16:29
 */
@Data
@ApiModel("自提赠送活动添加DTO")
public class PickupGiftActivityAddDTO{

    @ApiModelProperty("活动说明")
    private String description;

    @ApiModelProperty("赠品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long giftId;

    @ApiModelProperty("商家信息")
    private List<PickupGiftShopDTO> shopList;

}
