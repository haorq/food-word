package com.meiyuan.catering.merchant.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mt
 * @date 2020/3/16 15:10
 **/
@ApiModel("店铺标签信息vo")
@Data
public class ShopTagInfoVo {
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("标签名称")
    private String tagName;
    @ApiModelProperty("店铺标签")
    private Long id;
}
