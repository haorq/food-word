package com.meiyuan.catering.core.vo.base;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private static final long serialVersionUID = 6970237621122006540L;

    @ApiModelProperty("数据子名")
    private String itemName;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("数据id")
    private Long itemId;

    @ApiModelProperty("数据子项code")
    private String itemCode;

    @ApiModelProperty("数据子项描述")
    private String itemDetails;
}
