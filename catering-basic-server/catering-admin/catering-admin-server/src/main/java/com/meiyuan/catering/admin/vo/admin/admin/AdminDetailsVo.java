package com.meiyuan.catering.admin.vo.admin.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.admin.vo.role.AdminRoleListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lhm
 * @date 2020/3/20 15:48
 **/
@Data
@ApiModel("管理员详情")
public class AdminDetailsVo implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "用户名/手机")
    private String username;

    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "添加时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "角色")
    private List<AdminRoleListVO> roleList;
}
