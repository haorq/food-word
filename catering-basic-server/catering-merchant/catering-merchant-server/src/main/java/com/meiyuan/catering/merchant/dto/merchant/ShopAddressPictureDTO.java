package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/5/19 0019 11:44
 * @Description 简单描述 : 店铺地址图片信息DTO
 * @Since version-1.1.0
 */
@Data
@ApiModel("店铺地址图片信息DTO")
public class ShopAddressPictureDTO {
    @ApiModelProperty("图片路径")
    private String url;

    @ApiModelProperty("图片宽")
    private String w;

    @ApiModelProperty("图片高")
    private String h;
}
