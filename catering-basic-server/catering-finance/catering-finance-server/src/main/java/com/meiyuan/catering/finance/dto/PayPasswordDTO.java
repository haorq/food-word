package com.meiyuan.catering.finance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/3/25
 */
@Data
@ApiModel("支付密码DTO")
public class PayPasswordDTO implements Serializable {

    @ApiModelProperty("密码")
    @NotBlank
    private String password;
}
