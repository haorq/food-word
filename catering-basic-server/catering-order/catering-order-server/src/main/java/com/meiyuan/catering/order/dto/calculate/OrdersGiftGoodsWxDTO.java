package com.meiyuan.catering.order.dto.calculate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 订单商品信息——微信
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ToString(callSuper = true)
@ApiModel("订单赠送商品信息——微信")
public class OrdersGiftGoodsWxDTO {
    @ApiModelProperty(value = "赠品活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long giftsActivityId;
    @ApiModelProperty("订单商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderGoodsId;
    @ApiModelProperty("商品图片")
    private String goodsPicture;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品购买数量")
    private Integer quantity;
    @ApiModelProperty("是否赠品（0：不是赠品[默认]；1：是赠品）")
    private Boolean gifts;
}
