package com.meiyuan.catering.admin.dto.admin.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("保存权限DTO")
public class CommonPermissionSaveParamsDTO implements Serializable {

    @ApiModelProperty("权限类型，0:商户pc，1：app")
    private Integer type;

    @ApiModelProperty("权限id集合")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> permissionIds;
}
