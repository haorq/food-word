package com.meiyuan.catering.merchant.dto.shop.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author MeiTao
 * @Description 时间范围（自提/配送）
 * @Date  2020/3/22 0022 18:15
 */
@Data
@ApiModel("时间范围（自提/配送）")
public class TimeRangeResponseDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
