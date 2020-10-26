package com.meiyuan.catering.admin.dto.admin.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhm
 * @date 2020/3/2 17:06
 **/
@Data
@ApiModel("新增字典Dto")
public class DictGroupAddDto {
    @ApiModelProperty("新增字典1，新增字典子项2")
    private Integer type;
    @ApiModelProperty("字典id")
    private Integer id;
    @ApiModelProperty("字典名")
    private String name;
    @ApiModelProperty("字典代码")
    private String code;
}
