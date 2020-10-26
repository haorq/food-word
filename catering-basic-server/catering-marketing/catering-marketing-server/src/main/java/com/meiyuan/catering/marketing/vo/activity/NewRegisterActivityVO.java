package com.meiyuan.catering.marketing.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName NewRegisterActivityVO
 * @Description 新用户注册活动奖励
 * @Author gz
 * @Date 2020/9/7 10:11
 * @Version 1.3.0
 */
@Data
public class NewRegisterActivityVO {

    @ApiModelProperty(value = "优惠券面额")
    private BigDecimal ticketAmount;
    @ApiModelProperty(value = "积分")
    private Integer integral;
    @ApiModelProperty(value = "是否全部是无门槛券")
    private Boolean noThreshold;
}
