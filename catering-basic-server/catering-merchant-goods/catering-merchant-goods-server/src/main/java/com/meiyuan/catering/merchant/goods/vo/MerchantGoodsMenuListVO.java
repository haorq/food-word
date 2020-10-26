package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：销售菜单商品列表返回参数
 * @author yy
 * @date 2020/7/8
 */
@Data
@ApiModel("销售菜单-商品选择列表")
public class MerchantGoodsMenuListVO {

    @ApiModelProperty(value = "商家商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品单位，例如件、盒( 天，月，年)")
    private String unit;

    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "1-平台推送 2-商家自创")
    private Integer goodsAddType;

    @ApiModelProperty(value = "1-平台推送 2-商家自创 字符串")
    private String categoryAddTypeStr;

    @ApiModelProperty(value = "商品编号")
    private String spuCode;

    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    @ApiModelProperty(value = "规格值")
    private String propertyValue;

    @ApiModelProperty(value = "1-统一规格 2-多规格")
    private Integer goodsSpecType;

}
