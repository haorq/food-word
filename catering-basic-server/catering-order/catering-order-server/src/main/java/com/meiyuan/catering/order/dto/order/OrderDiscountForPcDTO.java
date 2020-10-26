package com.meiyuan.catering.order.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详情优惠
 *
 * @author lh
 */
@Data
public class OrderDiscountForPcDTO {

    @ApiModelProperty("优惠明细")
    private List<OrderDiscountDetailForPcDTO> discountList;
    @ApiModelProperty("优惠金额合计")
    private BigDecimal discountTotalPrice;

}
