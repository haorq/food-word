package com.meiyuan.catering.order.dto.calculate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 订单计算请求参数DTO
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ToString(callSuper = true)
@ApiModel("团购计算请求参数——微信")
public class BulkCalculateParamDTO extends CalculateParamDTO {
    private static final long serialVersionUID = 3384982463060377530L;

    @ApiModelProperty(value = "营销商品Id，(【团购】结算时必传)")
    private Long goodsId;

    @ApiModelProperty(value = "商品单价，(【团购【结算时必传)")
    private BigDecimal goodsSalesPrice;

    @ApiModelProperty(value = "商品数量，(【团购】结算时必传)")
    private Integer goodsQuantity;

    @ApiModelProperty(value = "合计金额，(【团购】结算时必传)")
    private BigDecimal totalAmount;

}
