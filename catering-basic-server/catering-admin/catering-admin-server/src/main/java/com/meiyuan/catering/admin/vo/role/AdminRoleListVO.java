package com.meiyuan.catering.admin.vo.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName AdminRoleListVo
 * @Description Admin角色列表
 * @Author gz
 * @Date 2020/9/28 10:10
 * @Version 1.5.0
 */
@Data
public class AdminRoleListVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色描述")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "角色菜单权限集合")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> permissionList;

    private List<Long> parentList;
    @ApiModelProperty(value = "是否含有操作员")
    private Boolean hasOperator;

}
