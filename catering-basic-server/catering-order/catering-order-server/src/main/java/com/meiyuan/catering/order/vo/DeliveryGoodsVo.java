package com.meiyuan.catering.order.vo;

import lombok.Data;

/**
 * @author XXJ
 * @description 核销商品信息
 * @date 2020/6/1 14:44
 * @since v1.1.0
 */
@Data
public class DeliveryGoodsVo {
    /** 商品名称 */
    private String goodsName;
    /** 商品数量 */
    private Integer quantity;
    /** 商品规格 */
    private String goodsSkuDesc;
}
