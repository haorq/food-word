package com.meiyuan.catering.admin.dto.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName AdminRoleAddDTO
 * @Description
 * @Author gz
 * @Date 2020/9/28 10:29
 * @Version 1.5.0
 */
@Data
public class AdminRoleDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @NotBlank(message = "角色描述不能为空")
    @ApiModelProperty(value = "角色描述")
    private String remark;
}
