package com.meiyuan.catering.user.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author yaoozu
 * @description 购物车
 * @date 2020/3/2612:18
 * @since v1.0.0
 */
@Data
@ApiModel("购物车DTO")
public class CartDTO implements Serializable {
    private static final long serialVersionUID = -3192534623367528376L;
    @ApiModelProperty("总金额")
    private BigDecimal totalAmt;

    @ApiModelProperty("原价总金额")
    private BigDecimal totalOldAmt;

    @ApiModelProperty("总包装费")
    private BigDecimal totalPackAmt;

    @ApiModelProperty("购物车数量")
    private Integer goodsNum;

    @ApiModelProperty("普通商品/菜单列表")
    private List<CartGoodsDTO> goodsList;

    @ApiModelProperty("拼单商品列表")
    private List<CartShareBillGoodsDTO> shareBillGoodsList;


    public BigDecimal getTotalAmt() {
        if (totalAmt == null) {
            totalAmt = BigDecimal.ZERO;
        }
        if (totalPackAmt == null) {
            totalPackAmt = BigDecimal.ZERO;
        }
        return totalAmt.add(totalPackAmt);
    }
}
