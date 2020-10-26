package com.meiyuan.catering.admin.vo.admin.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/18 16:50
 **/
@Data
@ApiModel("数据子项")
public class DicIntemVo implements Serializable {

    @ApiModelProperty("数据子名")
    private String itemName;

    @ApiModelProperty("数据子项code")
    private String itemCode;
}
