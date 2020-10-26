package com.meiyuan.catering.order.dto.query.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单商品信息——商户
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单赠送商品信息——商户")
public class OrdersGiftGoodsMerchantDTO {
    @ApiModelProperty("订单商品ID")
    private Long orderGoodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品购买数量")
    private Integer quantity;
}
