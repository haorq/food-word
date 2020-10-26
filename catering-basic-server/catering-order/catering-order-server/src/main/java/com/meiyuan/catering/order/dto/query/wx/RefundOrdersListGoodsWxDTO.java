package com.meiyuan.catering.order.dto.query.wx;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 功能描述:
 * @author xie-xi-jie
 * @date 2020/6/23 14:09
 * @since v 1.1.0
 */
@Data
@ApiModel("退款订单列表商品信息——微信")
public class RefundOrdersListGoodsWxDTO {
    @ApiModelProperty("订单商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderGoodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品购买数量")
    private Integer quantity;
    @ApiModelProperty("金额")
    private BigDecimal fee;
    @ApiModelProperty("商品图片")
    private String goodsPicture;
    @ApiModelProperty("商品规格描述")
    private String goodsSpecificationDesc;
    @ApiModelProperty("商品类型 1：普通商品；2：菜单；3：拼单；4：秒杀；5：团购；6—菜单拼单")
    private Integer goodsType;
}
