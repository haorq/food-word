package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/2 11:49
 */
@Data
@ApiModel("小程序类目选择商品分页列表")
public class MerchantGoodsWxCategoryPageVO {

    @ApiModelProperty(value = "门店商品规格(SPU)id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopGoodsId;

    @ApiModelProperty(value = "商家商品扩展id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "商家商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "门店id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;

    @ApiModelProperty(value = "商品来源 1-平台推送2-商家自创3-门店自创")
    private Integer goodsAddType;

    @ApiModelProperty(value = "商品来源中文")
    private String goodsAddTypeStr;
}
