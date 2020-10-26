package com.meiyuan.catering.merchant.dto.shop.config;

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
public class ShopConfigUpdateDTO {
    @ApiModelProperty("店铺id")
    private Long id;

    @ApiModelProperty("业务支持：1：仅配送，2：仅自提，3：全部")
    private Integer businessSupport;
}
