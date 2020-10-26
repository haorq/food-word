package com.meiyuan.catering.wx.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yaoozu
 * @description 商户配置信息
 * @date 2020/3/2814:45
 * @since v1.0.0
 */
@Data
@ApiModel("商户配置信息")
public class WxMerchantConfigInfoDTO {
    @ApiModelProperty(value = "配送价格")
    private BigDecimal deliveryPrice;

    @ApiModelProperty(value = "满单免配送金额")
    private BigDecimal freeDeliveryPrice;

    @ApiModelProperty(value = "订单起送金额")
    private BigDecimal leastDeliveryPrice;

    @ApiModelProperty(value = "业务支持：1：仅配送，2：仅自提，3：全部")
    private Integer businessSupport;

    @ApiModelProperty(value = "门店图片")
    private List<String> shopImgList;

}
