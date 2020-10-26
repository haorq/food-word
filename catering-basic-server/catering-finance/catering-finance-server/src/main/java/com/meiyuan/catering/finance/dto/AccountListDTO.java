package com.meiyuan.catering.finance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020-03-23
 */
@Data
@ApiModel("金额DTO")
public class AccountListDTO implements Serializable {

    @ApiModelProperty("充值金额")
    @DecimalMin(value = "1",message = "充值金额必须大于或等于1")
    @DecimalMax(value = "9999999",message = "充值金额必须小于或等于9999999")
    private BigDecimal rechargeAccount;
    @ApiModelProperty("赠送金额")
    @DecimalMin(value = "0",message = "赠送金额必须大于或等于0")
    @DecimalMax(value = "9999999",message = "赠送金额必须小于或等于9999999")
    private BigDecimal givenAccount;

}
