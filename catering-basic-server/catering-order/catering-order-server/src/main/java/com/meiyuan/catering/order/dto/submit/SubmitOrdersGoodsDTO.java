package com.meiyuan.catering.order.dto.submit;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述: 订单提交商品信息DTO
 * @author: XiJie Xie
 * @date: 2020/3/10 10:45
 * @version v 1.0
 */
@Data
public class SubmitOrdersGoodsDTO implements Serializable {
    private static final long serialVersionUID = -59717385461451243L;

    /** 商品ID */
    private String goodsId;
    /** 商品SKU编码 */
    private String goodsSkuCode;
    /** 商品购买数量 */
    private Integer quantity;
}
