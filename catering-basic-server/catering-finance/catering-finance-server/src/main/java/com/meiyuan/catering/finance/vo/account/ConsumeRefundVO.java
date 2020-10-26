package com.meiyuan.catering.finance.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/5/14
 */
@Data
@ApiModel("消费/退款")
public class ConsumeRefundVO implements Serializable {
    @ApiModelProperty("已退款金额")
    private BigDecimal income;
    @ApiModelProperty("已消费金额")
    private BigDecimal expend;
}
