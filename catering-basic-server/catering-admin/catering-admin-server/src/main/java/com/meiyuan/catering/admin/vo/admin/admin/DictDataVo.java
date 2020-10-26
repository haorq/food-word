package com.meiyuan.catering.admin.vo.admin.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 字典数据
 * </p>
 *
 * @author luohuan
 * @since 2019-12-25
 */
@Data
@ApiModel(value = "字典数据查询Vo")
public class DictDataVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("字典类型ID")
    private Integer groupId;

    @ApiModelProperty("数据名称")
    private String name;

    @ApiModelProperty("数据code")
    private String code;

    @ApiModelProperty("数据子项名称")
    private List<DicIntemVo> vos;




}
