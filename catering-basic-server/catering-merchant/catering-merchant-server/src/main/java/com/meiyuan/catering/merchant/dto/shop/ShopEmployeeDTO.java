package com.meiyuan.catering.merchant.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author fql
 */
@Data
public class ShopEmployeeDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("员工姓名")
    private String name;
    @ApiModelProperty("员工电话")
    private String phone;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("员工账号")
    private String accountNumber;
    @ApiModelProperty("角色id:多角色以逗号隔开")
    private String roleId;
    @ApiModelProperty("角色名称")
    private String roleName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("邮箱")
    private String email ;
    @ApiModelProperty("状态:0：禁用；1：启用")
    private Integer status;
    @ApiModelProperty("删除标记: 0：未删除；1：删除")
    private Integer isDel;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
