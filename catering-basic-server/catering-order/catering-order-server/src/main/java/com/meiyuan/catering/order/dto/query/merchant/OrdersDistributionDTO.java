package com.meiyuan.catering.order.dto.query.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单分布情况请求集合——商户
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单分布情况请求集合——商户")
public class OrdersDistributionDTO {
    @ApiModelProperty("预约时间：yyyy-mm-dd")
    private String estimateTime;
    @ApiModelProperty("是否存在预约订单")
    private Boolean flag;
}
