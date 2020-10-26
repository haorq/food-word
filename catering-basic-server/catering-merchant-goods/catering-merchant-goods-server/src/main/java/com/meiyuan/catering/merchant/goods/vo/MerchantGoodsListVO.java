package com.meiyuan.catering.merchant.goods.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author lhm
 * @date 2020/7/6
 * @description
 **/
@Data
@ApiModel("商户商品列表返回vo")
public class MerchantGoodsListVO implements Serializable {

    @ApiModelProperty(value = "商家商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodId;

    @ApiModelProperty(value = "商家商品编号")
    private String spuCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品详情图片")
    private String infoPicture;

    @ApiModelProperty(value = "标签集合")
    private List<String> labelList;


    @ApiModelProperty(value = "1-下架,2-上架")
    private Integer merchantGoodsStatus;


    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "原价")
    private BigDecimal marketPrice;

    @ApiModelProperty("最小原价是否null")
    private Boolean nullMinMarketPrice;

    @ApiModelProperty(value = "现价")
    private BigDecimal salesPrice;

    @ApiModelProperty(value = "企业价（多个企业价最低的那个）")
    private BigDecimal enterprisePrice;

    @ApiModelProperty(value = "1-平台推送2-商家自创")
    private Integer goodsAddType;
    @ApiModelProperty(value = "1-统一规格 2-多规格")
    private Integer goodsSpecType;

    @ApiModelProperty(value = "是否参加促销活动")
    private Boolean isJoinActivity;

}
