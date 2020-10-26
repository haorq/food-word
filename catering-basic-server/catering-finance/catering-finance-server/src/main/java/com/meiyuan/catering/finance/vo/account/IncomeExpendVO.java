package com.meiyuan.catering.finance.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/3/25
 */
@Data
@ApiModel("收入/支出")
public class IncomeExpendVO implements Serializable {
    @ApiModelProperty("收入")
    private BigDecimal income;
    @ApiModelProperty("支出")
    private BigDecimal expend;
}
