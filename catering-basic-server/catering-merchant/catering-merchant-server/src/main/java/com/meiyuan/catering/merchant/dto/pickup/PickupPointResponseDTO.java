package com.meiyuan.catering.merchant.dto.pickup;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Description 店铺自提点信息
 * @Date  2020/3/24 0024 12:04
 */
@Data
public class PickupPointResponseDTO implements Serializable {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty(value = "自提点id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pickupId;

    @ApiModelProperty(value = "负责人姓名")
    private String chargerName;

    @ApiModelProperty(value = "负责人电话")
    private String chargerPhone;

    @ApiModelProperty(value = "自提点名")
    private String name;

    @ApiModelProperty(value = "完整地址")
    private String addressFull;

    @ApiModelProperty(value = "店铺状态:1：启用，2：禁用【1.2.0】")
    private Integer shopStatus;

}
