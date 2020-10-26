package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：店铺关联信息
 * @author yy
 * @date 2020/7/8
 */
@Data
@ApiModel("销售菜单-店铺关联")
public class MenuShopGoodsRelationVO {

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;

    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;
}
