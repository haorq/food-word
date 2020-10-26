package com.meiyuan.catering.goods.dto.monthsales;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author wxf
 * @date 2020/5/20 11:09
 * @description 简单描述
 **/
@Data
public class GoodsMonthSalesDTO {
    private Long id;
    /**
     * 时间-年月日
     */
    private LocalDate time;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * sku_code
     */
    private String skuCode;
    /**
     * 数量
     */
    private Long number;
    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 门店id
     */
    private Long shopId;
}
