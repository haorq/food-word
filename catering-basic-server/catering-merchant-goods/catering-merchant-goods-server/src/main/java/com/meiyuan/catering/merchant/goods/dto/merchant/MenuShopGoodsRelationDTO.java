package com.meiyuan.catering.merchant.goods.dto.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yy
 * @date 2020/7/8
 */
@Data
public class MenuShopGoodsRelationDTO {

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;
}
