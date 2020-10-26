package com.meiyuan.catering.merchant.goods.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author lhm
 * @date 2020/7/5
 * @description
 **/
@Data
@ApiModel("门店修改 dto")
public class ShopGoodsUpdateDTO implements Serializable {

    @ApiModelProperty(value = "门店id",hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "商品id不能为空")
    private Long goodsId;
    @ApiModelProperty("店铺sku集合")
    private List<ShopSkuDTO> shopSkuDTOS;

}
