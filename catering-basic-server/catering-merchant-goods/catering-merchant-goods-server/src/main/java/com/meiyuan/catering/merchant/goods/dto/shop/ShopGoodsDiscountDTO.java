package com.meiyuan.catering.merchant.goods.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author lhm
 * @date 2020/7/5
 * @description
 **/
@Data
@ApiModel("门店最低折扣信息dto")
public class ShopGoodsDiscountDTO implements Serializable {

    @ApiModelProperty(value = "门店id",hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "商品最低折扣")
    private Double price;
}
