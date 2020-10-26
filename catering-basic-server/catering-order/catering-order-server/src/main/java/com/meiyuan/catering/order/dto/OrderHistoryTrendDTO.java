package com.meiyuan.catering.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OrderHistoryTrendDTO {

    @ApiModelProperty("订单id")
    private Long id;
    @ApiModelProperty("营业额")
    private BigDecimal turnover;
    @ApiModelProperty("订单数")
    private Integer orderNum;
    @ApiModelProperty("营业日期")
    private String businessDay;

    public OrderHistoryTrendDTO(BigDecimal turnover, Integer orderNum, String businessDay) {
        this.turnover = turnover;
        this.orderNum = orderNum;
        this.businessDay = businessDay;
    }

    public OrderHistoryTrendDTO() {
    }
}
