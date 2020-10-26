package com.meiyuan.catering.merchant.dto.shop.bill;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopBillTotalDTO {

    private BigDecimal totalOrderCount;

    private BigDecimal totalOrderAmount;

    private BigDecimal totalOrderIncome;

    private BigDecimal totalRefundAmount;

    private BigDecimal totalRefundCount;


    private BigDecimal totalPlatformDiscount;

    private BigDecimal totalMerchantDiscount;

    private BigDecimal totalMerchantIncome;

    private BigDecimal totalShopSellGoods;

    private BigDecimal totalNewShop;

    private Integer countShop;

    public ShopBillTotalDTO(){}

}
