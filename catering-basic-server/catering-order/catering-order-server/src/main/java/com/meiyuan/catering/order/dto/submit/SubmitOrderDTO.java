package com.meiyuan.catering.order.dto.submit;

import com.meiyuan.catering.order.dto.calculate.OrderCalculateParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 功能描述: 订单提交信息Vo
 * @author: XiJie Xie
 * @date: 2020/3/10 10:45
 * @version v 1.0
 */
@Data
@ToString(callSuper = true)
@ApiModel("提交订单DTO")
public class SubmitOrderDTO extends OrderCalculateParamDTO implements Serializable {
    private static final long serialVersionUID = 247614338042746950L;

    @ApiModelProperty(value = "卡路里")
    private BigDecimal calories;
    @ApiModelProperty(value = "餐具数量")
    private Integer tableware;
    @ApiModelProperty(value = "推荐人ID")
    private String referrerId;
    @ApiModelProperty(value = "订单备注")
    private String remarks;
}
