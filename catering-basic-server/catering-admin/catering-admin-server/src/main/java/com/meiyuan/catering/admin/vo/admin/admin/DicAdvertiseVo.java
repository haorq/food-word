package com.meiyuan.catering.admin.vo.admin.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/23 10:46
 **/
@Data
@ApiModel("广告字典vo")
public class DicAdvertiseVo implements Serializable {
    @ApiModelProperty(value = "广告id")
    private Integer id;

    @ApiModelProperty(value = "广告值")
    private String url;
}
