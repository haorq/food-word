package com.meiyuan.catering.order.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单详情优惠明细
 *
 * @author lh
 */
@Data
public class OrderDiscountDetailForPcDTO {

    @ApiModelProperty("优惠方式")
    private String discountType;
    @ApiModelProperty("优惠对象")
    private String discountObj;
    @ApiModelProperty("优惠数量")
    private Integer discountAmount;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountPrice;

}
