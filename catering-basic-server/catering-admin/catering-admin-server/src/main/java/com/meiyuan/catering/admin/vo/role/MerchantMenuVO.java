package com.meiyuan.catering.admin.vo.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MerchantMenuVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("权限id")
    private Long id;
    @ApiModelProperty("权限名称")
    private String label;
    @ApiModelProperty("权限标识")
    private String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("父权限id")
    private Long parentId;
    @ApiModelProperty("排序")
    private Integer sort;
}
