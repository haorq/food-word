package com.meiyuan.catering.order.dto.query.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author XiJie-Xie
 * @email 1121075903@qq.com
 * @create 2020/5/8 9:44
 */
@Data
public class EstimateTimeCheckDTO {

    @ApiModelProperty(value = "描述信息")
    private String describe;
    @ApiModelProperty(value = "是否是当天取餐的订单（true：是，false：否）")
    private Boolean toDay;
}
