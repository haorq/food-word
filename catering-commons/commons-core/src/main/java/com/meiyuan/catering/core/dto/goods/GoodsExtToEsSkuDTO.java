package com.meiyuan.catering.core.dto.goods;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName GoodsExtToEsSkuDTO
 * @Description
 * @Author gz
 * @Date 2020/7/13 15:49
 * @Version 1.2.0
 */
@Data
public class GoodsExtToEsSkuDTO {
    /**
     * 原价
     */
    private BigDecimal marketPrice;
    /**
     * 商品sku
     */
    private String skuCode;
    /**
     * 商品规格值
     */
    private String propertyValue;
    /**
     * 现价
     */
    private BigDecimal salesPrice;
    /**
     * 企业价
     */
    private BigDecimal enterprisePrice;
    /**
     * 每日剩余库存
     */
    private Integer remainStock;
}
