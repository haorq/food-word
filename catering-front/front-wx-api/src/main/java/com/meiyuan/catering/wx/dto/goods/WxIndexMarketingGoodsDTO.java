package com.meiyuan.catering.wx.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yaoozu
 * @description 微信首页 秒杀/团购 商品信息
 * @date 2020/3/309:40
 * @since v1.0.0
 */
@Data
@ApiModel("微信首页 秒杀/团购 商品信息")
public class WxIndexMarketingGoodsDTO {
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty(value = "活动商品表主键ID ES中作为ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long mGoodsId;
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("列表图片")
    private String listPicture;
    @ApiModelProperty("名称")
    private String goodsName;

    @ApiModelProperty("原价(市场价)")
    private BigDecimal marketPrice;
    @ApiModelProperty("销售价")
    private BigDecimal salesPrice;

    @ApiModelProperty("标签")
    private String label;

}
