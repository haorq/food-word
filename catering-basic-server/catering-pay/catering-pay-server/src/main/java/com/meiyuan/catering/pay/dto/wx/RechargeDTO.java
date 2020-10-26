package com.meiyuan.catering.pay.dto.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 充值参数
 *
 * @author zengzhangni
 * @date 2020-03-27
 */
@Data
@ApiModel("充值DTO")
public class RechargeDTO implements Serializable {

    @ApiModelProperty("充值规则id")
    @NotNull
    private Long rechargeRuleId;
    @ApiModelProperty("充值活动id")
    private Long activityId;

}
