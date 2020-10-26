package com.meiyuan.catering.merchant.dto.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/16 14:43
 **/
@Data
@ApiModel("店铺标签添加 参数DTO")
public class ShopTagAddDTO implements Serializable {


    @ApiModelProperty("1：添加 2：编辑")
    private Integer type;
    @ApiModelProperty("只有编辑传id")
    private Long id;
    @ApiModelProperty("标识名称")
    private String tagName;
    @ApiModelProperty("软删除标记，0为正常，1为删除")
    private Boolean del;
    @ApiModelProperty("创建人")
    private Long createBy;
}
