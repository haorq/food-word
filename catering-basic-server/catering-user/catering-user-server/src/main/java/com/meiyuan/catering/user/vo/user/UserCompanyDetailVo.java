package com.meiyuan.catering.user.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/5/20
 * @description
 **/
@Data
@ApiModel("企业用户详情vo")
public class UserCompanyDetailVo implements Serializable {

    @ApiModelProperty("企业用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("电话")
    private String account;
    @ApiModelProperty("状态")
    private Integer companyStatus;
    @ApiModelProperty("密码")
    private String password;
}
