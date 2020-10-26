package com.meiyuan.catering.user.dto.cart;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 修改购物车
 * @date 2020/4/1510:49
 * @since v1.0.0
 */
@Data
public class ModifyCartDTO {
    @ApiModelProperty(hidden = true, value = "新增 true 反则 修改")
    private Boolean flag;
    @ApiModelProperty("id")
    private Long goodsId;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("菜品分类id")
    private Long categoryId;

    @ApiModelProperty("上下架 1-下架,2-上架")
    private Integer goodsStatus;
}
