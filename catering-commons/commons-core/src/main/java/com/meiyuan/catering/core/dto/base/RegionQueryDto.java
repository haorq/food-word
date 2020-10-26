package com.meiyuan.catering.core.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date  2020/3/2 0002 16:52
 */
@Data
public class RegionQueryDto {

    @ApiModelProperty(value = "市：1，区：2")
    private Integer type;
    @ApiModelProperty(value = "地址编码")
    private String adressCode;
}
