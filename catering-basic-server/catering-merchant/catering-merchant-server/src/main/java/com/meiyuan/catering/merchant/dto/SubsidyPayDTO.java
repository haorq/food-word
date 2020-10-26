package com.meiyuan.catering.merchant.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/10/13 17:00
 * @since v1.5.0
 */
@Data
public class SubsidyPayDTO {


    @ApiModelProperty("平台应收取的配送费")
    BigDecimal platformAmount;
    @ApiModelProperty("本单补贴金额")
    BigDecimal subsidyAmount;

}
