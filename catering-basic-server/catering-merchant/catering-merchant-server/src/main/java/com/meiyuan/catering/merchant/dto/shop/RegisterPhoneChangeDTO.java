package com.meiyuan.catering.merchant.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/6/11 0011 13:56
 * @Description 简单描述 : 注册手机号修改DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("注册手机号修改DTO")
public class RegisterPhoneChangeDTO {
    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("门店注册电话（门店登录账号）")
    private String registerPhone;
}
