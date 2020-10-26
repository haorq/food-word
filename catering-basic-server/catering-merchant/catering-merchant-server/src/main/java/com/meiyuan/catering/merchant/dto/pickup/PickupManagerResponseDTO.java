package com.meiyuan.catering.merchant.dto.pickup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 自提点管理返回参数DTO
 * @Date  2020/3/24 0024 12:04
 */
@Data
@ApiModel("自提点管理返回参数DTO")
public class PickupManagerResponseDTO implements Serializable {
    @ApiModelProperty(value = "自提点是否绑定自身：true:是，false：否")
    private Boolean isPickupPoint;

    @ApiModelProperty(value = "自提点集合")
    private List<PickupPointResponseDTO> pointList;

}
