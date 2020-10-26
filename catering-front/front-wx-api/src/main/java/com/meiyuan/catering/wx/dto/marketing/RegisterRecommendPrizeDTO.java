package com.meiyuan.catering.wx.dto.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author luohuan
 * @date 2020/4/9
 **/
@Data
@ApiModel("新人注册推荐奖励规则DTO")
public class RegisterRecommendPrizeDTO {

    @ApiModelProperty("推荐人奖励积分)")
    private Integer referrerIntegral;

    @ApiModelProperty("被推荐人奖励积分（即好友应获取的奖励积分）")
    private Integer referralIntegral;
    @ApiModelProperty("优惠券金额")
    private BigDecimal ticketAmount;

    @ApiModelProperty("完成一单推荐人奖励积分)")
    private Integer referrerIntegralOne;
    @ApiModelProperty("完成一单优惠券金额")
    private BigDecimal ticketAmountOne;

    @ApiModelProperty(value = "0-注册成功；1-下单成功")
    private Integer conditionsRule;
}
