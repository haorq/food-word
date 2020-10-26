package com.meiyuan.catering.wx.dto.index;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 描述:
 *
 * @author zengzhangni
 * @date 2020/9/2 9:31
 * @since v1.4.0
 */
@Data
public class WxRecommendVO implements Serializable {

    @ApiModelProperty("goodsId")
    private String goodsId;
    @ApiModelProperty("名称")
    private String goodsName;

    @ApiModelProperty("门店id")
    private String shopId;
    @ApiModelProperty("门店名称")
    private String shopName;
    @ApiModelProperty("门头图")
    private String doorHeadPicture;

    @ApiModelProperty("详情图片")
    private String infoPicture;

    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("现价")
    private BigDecimal salesPrice;

    @ApiModelProperty("标签")
    private String labelStr;

    @ApiModelProperty("折扣")
    private String discountLabel;

    @ApiModelProperty("距离")
    private Double distance;

    @ApiModelProperty("距离")
    private String distanceStr;
}
