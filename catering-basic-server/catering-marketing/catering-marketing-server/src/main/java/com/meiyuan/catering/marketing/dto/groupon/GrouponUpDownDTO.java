package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author luohuan
 * @date 2020/3/16
 **/
@Data
@ApiModel("团购上下架DTO")
public class GrouponUpDownDTO {

    @NotNull(message = "团购活动ID不能为空")
    @ApiModelProperty("团购活动ID")
    private Long id;

    @NotNull(message = "上下架状态不能为空")
    @ApiModelProperty("上下架状态(1：下架，2：上架)")
    private Integer upDown;
}
