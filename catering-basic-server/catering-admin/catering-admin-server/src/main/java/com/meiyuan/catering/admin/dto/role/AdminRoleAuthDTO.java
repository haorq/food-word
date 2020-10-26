package com.meiyuan.catering.admin.dto.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @ClassName AdminRoleAuthDTO
 * @Description
 * @Author gz
 * @Date 2020/9/28 10:33
 * @Version 1.5.0
 */
@Data
public class AdminRoleAuthDTO {
    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty(value = "角色ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId;
    @NotNull
    @Size(min = 1,message = "请至少选择一个菜单")
    @ApiModelProperty(value = "关联菜单集合")
    private List<Long> menuList;
}
