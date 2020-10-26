package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 商户添加参数DTO
 * @Date  2020/3/12 0012 16:20
 */
@Data
@ApiModel("商户添加参数DTO")
public class VerifyShopInfoDTO {
    @ApiModelProperty("店铺名/注册手机号/详细地址/店铺编码")
    String keyword;
    @ApiModelProperty("是否是店铺：true:是，false：否（自提点）")
    Boolean type;
}
