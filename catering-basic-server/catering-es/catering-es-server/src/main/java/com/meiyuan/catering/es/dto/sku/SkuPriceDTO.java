package com.meiyuan.catering.es.dto.sku;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/7/28 18:22
 * @since v1.1.0
 */
@Data
public class SkuPriceDTO {

    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("销售价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;

}
