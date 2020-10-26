package com.meiyuan.catering.admin.dto.admin.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author lhm
 * @date 2020/3/20 14:04
 **/
@Data
@ApiModel("管理员编辑dto")
public class AdminUpdateDTO  implements Serializable {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("管理员名称")
    private String username;
    @ApiModelProperty("联系电话")
    private String phone;
    @ApiModelProperty("管理员密码")
    private String password;
    @ApiModelProperty("状态:1--正常，2--禁用")
    private Integer status;
    @ApiModelProperty("删除状态:0--正常，1--删除")
    private Boolean isDel;
    @ApiModelProperty("角色")
    private List<Long> roleList;
}
