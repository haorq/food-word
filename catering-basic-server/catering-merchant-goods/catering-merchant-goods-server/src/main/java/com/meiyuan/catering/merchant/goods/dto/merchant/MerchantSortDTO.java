package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/9/1 11:55
 */
@Data
@ApiModel("商户分类-APP--置顶")
public class MerchantSortDTO {
    @ApiModelProperty("分类置顶时，只传分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;

    @ApiModelProperty("商品置顶时，传商品id和分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

}
