package com.meiyuan.catering.admin.vo.admin.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhm
 * @date 2020/3/18 16:11
 **/
@Data
@ApiModel("所有字典结果vo")
public class DicAllVo {

    @ApiModelProperty("字典id")
    private Long id;

    @ApiModelProperty("数据名称")
    private String name;

    @ApiModelProperty("数据值")
    private String code;
}
