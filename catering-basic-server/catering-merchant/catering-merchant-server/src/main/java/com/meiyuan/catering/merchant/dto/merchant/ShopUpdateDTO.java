package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 店铺基本信息更新dto
 * @Date  2020/3/16 0016 14:53
 */
@Data
@ApiModel("店铺基本信息更新dto")
public class ShopUpdateDTO {
    @ApiModelProperty("店铺id")
    private Long id;

    @ApiModelProperty("是否是自提点：0- 不是 1-是")
    private Boolean isPickupPoint;

    @ApiModelProperty("售卖模式 ： 1-菜单售卖模式 2-商品售卖模式")
    private Integer sellType;

    @ApiModelProperty("售卖模式 ： 1-菜单售卖模式 2-商品售卖模式")
    private Integer businessStatus;
}
