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
 * @date 2020/3/20 12:47
 **/
@Data
@ApiModel("管理员添加dto")
public class AdminAddDTO implements Serializable {


    @ApiModelProperty("管理员名称")
    private String username;
    @ApiModelProperty("联系电话")
    private String phone;
    @ApiModelProperty("管理员密码")
    private String password;
    @ApiModelProperty("状态:1--正常，2--禁用")
    private Integer status;
    @NotNull
    @Size(min = 1,message = "请至少选择一个角色")
    @ApiModelProperty("角色")
    private List<Long> roleList;

}
