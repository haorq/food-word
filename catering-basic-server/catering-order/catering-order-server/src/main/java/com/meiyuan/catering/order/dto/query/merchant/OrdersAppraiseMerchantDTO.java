package com.meiyuan.catering.order.dto.query.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单评价表(CateringOrdersAppraise)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("门店总体评价信息——后台")
public class OrdersAppraiseMerchantDTO {
    @ApiModelProperty("总评分")
    private BigDecimal totalScore;
    @ApiModelProperty("口味评分")
    private BigDecimal tasteScore;
    @ApiModelProperty("服务评分")
    private BigDecimal serviceScore;
    @ApiModelProperty("包装评分")
    private BigDecimal packScore;

}
