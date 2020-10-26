package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Date 2020/10/9 0009 16:55
 * @Description 简单描述 :
 * @Since version-1.5.0
 */
@Data
public class AppJwtTokenDTO implements Serializable {
    @ApiModelProperty(value = "门店id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "当前登录账号对应id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long accountTypeId;
    @ApiModelProperty(value = "账号类型")
    private Integer accountType;
    @ApiModelProperty(value = "仅员工登录时存在【1.5.0】",hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long employeeId;}
