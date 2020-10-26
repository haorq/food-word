package com.meiyuan.catering.user.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/19 17:04
 **/
@Data
@ApiModel("企业用户编辑Dto")
public class UserCompanyExitDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("企业用户id")
    private Long id;
    @ApiModelProperty("联系人")
    private String name;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("企业用户状态 1启用 2禁用")
    private Integer companyStatus;
    @ApiModelProperty(" 0：正常；1：删除")
    private Boolean del;
    @ApiModelProperty("操作类型：1 编辑 2 删除")
    private Integer type ;

}
