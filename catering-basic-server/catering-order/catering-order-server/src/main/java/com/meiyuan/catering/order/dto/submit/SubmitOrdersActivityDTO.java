package com.meiyuan.catering.order.dto.submit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述: 订单活动创建信息DTO
 * @author: XiJie Xie
 * @date: 2020/3/10 10:45
 * @version v 1.0
 */
@Data
@ApiModel
public class SubmitOrdersActivityDTO implements Serializable {
    private static final long serialVersionUID = 3850875823890938652L;

    @ApiModelProperty(value = "营销活动类型（1：秒杀；2：拼团；3：赠品活动）")
    private Integer activityType;

    @ApiModelProperty(value = "营销活动ID")
    private String activityId;
}
