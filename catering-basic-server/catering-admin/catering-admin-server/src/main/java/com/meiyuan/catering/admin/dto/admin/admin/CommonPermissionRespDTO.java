package com.meiyuan.catering.admin.dto.admin.admin;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("权限树DTO")
public class CommonPermissionRespDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("父菜单Id,顶级菜单为0")
    private Long parentId;

    @ApiModelProperty("路径")
    private String url;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("权限标识")
    private String code;

    @ApiModelProperty("是否选中")
    private boolean checkFlag;

    @ApiModelProperty("子菜单")
    private List<CommonPermissionRespDTO> children;
}
