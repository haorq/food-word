package com.meiyuan.catering.wx.dto.merchant.v13;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商家优惠信息
 *
 * @author zengzhangni
 * @date 2020/8/10 10:14
 * @since v1.3.0
 */
@Data
@ApiModel("微信商户优惠dto")
public class WxMerchantActivityVO implements Serializable {

    @ApiModelProperty(value = "普通商品最低折扣")
    private String ordinaryLowestDiscount;
    @ApiModelProperty(value = "秒杀商品最低金额")
    private String seckillLowestAmount;
    @ApiModelProperty(value = "团购商品最低金额")
    private String groupLowestAmount;
    @ApiModelProperty(value = "领卷信息")
    private String couponStr;
    @ApiModelProperty(value = "满减信息")
    private String deductionStr;
    @ApiModelProperty(value = "满x元免配送费")
    private String dispatchingStr;

}
