package com.meiyuan.catering.merchant.dto.goods.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 商品列表请求参数
 * @date 2020/3/2117:55
 * @since v1.0.0
 */
@Data
@ApiModel("商品列表请求参数DTO")
public class MerchantGoodsListReqDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "商品分类ID")
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品列表图")
    private String listPicture;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品状态：1--出售中，2--已下架")
    private Integer goodsStatus;
}
