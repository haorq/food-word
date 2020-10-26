package com.meiyuan.catering.merchant.dto.gift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 商户赠品 查询条件TO
 * @Date  2020/3/16 0016 14:53
 */
@Data
@ApiModel("商户赠品 查询条件TO")
public class ShopGiftDTO {
    @ApiModelProperty("店铺id")
    private Long shopId;
}
