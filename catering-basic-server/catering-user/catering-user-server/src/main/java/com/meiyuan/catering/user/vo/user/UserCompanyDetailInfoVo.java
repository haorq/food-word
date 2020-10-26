package com.meiyuan.catering.user.vo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/23 17:34
 **/
@Data
@ApiModel("企业用户信息详情")
public class UserCompanyDetailInfoVo implements Serializable {

    @ApiModelProperty("企业用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("企业名称")
    private String companyName;
    @ApiModelProperty("完整地址")
    private String addressFull;
    @ApiModelProperty("联系人")
    private String contantName;
    @ApiModelProperty("电话")
    private String account;


}
