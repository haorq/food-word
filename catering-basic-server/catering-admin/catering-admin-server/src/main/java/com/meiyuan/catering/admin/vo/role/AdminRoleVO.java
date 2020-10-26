package com.meiyuan.catering.admin.vo.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName AdminRoleAddDTO
 * @Description
 * @Author gz
 * @Date 2020/9/28 10:29
 * @Version 1.5.0
 */
@Data
public class AdminRoleVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色描述")
    private String remark;
}
