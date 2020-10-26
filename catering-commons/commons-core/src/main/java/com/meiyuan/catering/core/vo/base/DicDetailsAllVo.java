package com.meiyuan.catering.core.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lhm
 * @date 2020/3/30 17:04
 **/
@Data
@ApiModel(value = "字典数据查询Vo")
public class DicDetailsAllVo implements Serializable {

    private static final long serialVersionUID = 3462129533944839609L;
    @ApiModelProperty("字典类型ID")
    private Integer groupId;

    @ApiModelProperty("数据名称")
    private String name;

    @ApiModelProperty("数据code")
    private String code;

    @ApiModelProperty("数据子项名称")
    private List<DicIntemVo> vos;

}
