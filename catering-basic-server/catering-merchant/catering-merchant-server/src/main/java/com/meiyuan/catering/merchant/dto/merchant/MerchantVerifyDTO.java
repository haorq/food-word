package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/6/11 0011 13:56
 * @Description 简单描述 : 商户信息验重接收参数DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("商户信息验重接收参数DTO")
public class MerchantVerifyDTO {
    @ApiModelProperty("商户名称")
    private String merchantName;
    @ApiModelProperty("联系电话")
    private String phone;
}
