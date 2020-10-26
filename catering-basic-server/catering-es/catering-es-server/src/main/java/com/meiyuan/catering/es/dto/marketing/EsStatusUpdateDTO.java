package com.meiyuan.catering.es.dto.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/8/3 0003 15:31
 * @Description 简单描述 : 店铺、商户状态修改dto
 * @Since version-1.3.0
 */
@Data
@ApiModel(value = "店铺、商户状态修改dto")
public class EsStatusUpdateDTO {
    @ApiModelProperty("店铺、商户id")
    private Long id;

    @ApiModelProperty(value = "状态 ：1 ：启用，2 ： 禁用")
    private Integer status;

    @ApiModelProperty(value = "类型 ：1 ：商户，2 ：店铺")
    private Integer type;
}
