package com.meiyuan.catering.order.dto.goods;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author wxf
 * @date 2020/4/22 9:40
 * @description 商品月销量
 **/
@Data
public class GoodsMonthSalesDTO {
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
     * 商品类型（1--菜单商品，2--普通商品，3--拼单商品，4--秒杀商品；5--团购商品；6—菜单拼单）
     */
    private Integer goodsType;
    /**
     * 店铺id
     */
    private Long shopId;
}
