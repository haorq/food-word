package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.user.dto.user.UserLoginDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 购物车商品
 * @date 2020/3/2516:51
 * @since v1.0.0
 */
@Data
public class CartGoodsQueryDTO extends BasePageDTO {
    @ApiModelProperty("商户id 拼单不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户", hidden = true)
    private Integer userType;
    @ApiModelProperty(value = "用户ID/企业用户ID", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @ApiModelProperty("商品id 拼单不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("菜单ID 菜单购物车才传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
    @ApiModelProperty("拼单号 拼单时传")
    private String shareBillNo;
    @ApiModelProperty(value = "购物车类型:1--普通，2--拼单", required = true)
    private Integer type;

    @ApiModelProperty("拼单 用户登录信息，便于组装数据")
    private UserLoginDTO userLoginDTO;
}
