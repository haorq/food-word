package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author yaoozu
 * @description 拼单购物车商品
 * @date 2020/3/2522:03
 * @since v1.0.0
 */
@Data
@ApiModel("拼单购物车商品DTO")
public class CartShareBillGoodsDTO implements Serializable {
    private static final long serialVersionUID = 3135737278316945719L;
    @ApiModelProperty("用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty("微信头像")
    private String avatar;

    @ApiModelProperty("微信昵称")
    private String nickname;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmt;

    @ApiModelProperty("原价总金额")
    private BigDecimal totalOldAmt;

    @ApiModelProperty("购物车数量")
    private Integer    goodsNum;

    @ApiModelProperty("商品列表")
    List<CartGoodsDTO> goodsList;

    public CartShareBillGoodsDTO(){
    }

    public CartShareBillGoodsDTO(Long userId,String nickname,String avatar){
        this.userId = userId;
        this.avatar = avatar;
        this.nickname = nickname;
    }
}
