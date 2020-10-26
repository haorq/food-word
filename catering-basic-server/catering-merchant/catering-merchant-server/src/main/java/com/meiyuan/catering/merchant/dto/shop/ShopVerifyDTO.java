package com.meiyuan.catering.merchant.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/6/11 0011 13:56
 * @Description 简单描述 : 店铺信息验重接收参数DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("店铺信息验重接收参数DTO")
public class ShopVerifyDTO {
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("店铺编码")
    private String shopCode;
    @ApiModelProperty("店铺名称:同一商户下的店铺名称不能重复")
    private String shopName;
    @ApiModelProperty("门店联系电话")
    private String shopPhone;
    @ApiModelProperty("门店联系人")
    private String primaryPersonName;
    @ApiModelProperty("门店注册电话（门店登录账号）")
    private String registerPhone;
    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("经纬度")
    private String mapCoordinate;
}
