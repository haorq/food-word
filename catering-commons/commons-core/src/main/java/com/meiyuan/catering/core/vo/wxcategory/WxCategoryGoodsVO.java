package com.meiyuan.catering.core.vo.wxcategory;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/4 14:34
 */
@Data
public class WxCategoryGoodsVO {

    @ApiModelProperty("商品id")
    private String goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty(value = "门店商品规格(SPU)id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopGoodsId;

    @ApiModelProperty(value = "商家商品扩展id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

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

    @ApiModelProperty(hidden = true,value = "门店商品是否删除")
    private Boolean del;

    @ApiModelProperty(hidden = true,value = "门店商品是否下架")
    private Integer shopGoodsStatus;

    @ApiModelProperty(hidden = true,value = "商户商品是否下架")
    private Integer merchantGoodsStatus;
}
