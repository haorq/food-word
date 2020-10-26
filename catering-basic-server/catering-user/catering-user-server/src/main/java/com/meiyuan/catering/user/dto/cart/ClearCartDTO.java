package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yaoozu
 * @description 清除购物车
 * @date 2020/3/2615:15
 * @since v1.0.0
 */

@Data
@ApiModel("清除购物车")
public class ClearCartDTO implements Serializable {
    @ApiModelProperty(value = "用户ID/企业用户ID", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户", hidden = true)
    private Integer userType;

    @ApiModelProperty(value = "商户售卖模式:1--菜单，2--商品", hidden = true)
    private Integer sellType;

    @ApiModelProperty(value = "商户ID", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "店铺ID", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "购物车类型:1--菜单，2--商品/秒杀，3--拼单", required = true)
    private Integer type;
    @ApiModelProperty(value = "商品ID", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty(value = "1-下架,2-上架", hidden = true)
    private Integer goodsStatus;

    @ApiModelProperty("菜单ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
    @ApiModelProperty("拼单号")
    private String shareBillNo;
}
