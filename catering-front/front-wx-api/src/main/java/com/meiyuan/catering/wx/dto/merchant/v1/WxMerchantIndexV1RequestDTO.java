package com.meiyuan.catering.wx.dto.merchant.v1;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 店铺首页
 * @date 2020/3/2718:19
 * @since v1.0.0
 */
@Data

public class WxMerchantIndexV1RequestDTO {
    @ApiModelProperty(value = "商户ID", required = true)
    private Long merchantId;
    @ApiModelProperty(value = "店铺ID", required = true)
    private Long shopId;

    @ApiModelProperty("拼单号")
    private String shareBillNo;

    @ApiModelProperty(value = "位置：经度,纬度")
    private String location;
}
