package com.meiyuan.catering.goods.dto.sku;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/19 16:50
 * @description 商品根据sku编码 信息
 **/
@Data
public class GoodsBySkuDTO {
    @ApiModelProperty("商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("菜品编号")
    private String spuCode;
    @ApiModelProperty("菜品名称")
    private String goodsName;
    @ApiModelProperty("菜品分类id")
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("列表图片")
    private String listPicture;
    @ApiModelProperty("详情图片")
    private String infoPicture;
    @ApiModelProperty("商品详细介绍，是富文本格式")
    private String goodsDescribeText;
    @ApiModelProperty("sku编码 新增不传")
    private String skuCode;
    @ApiModelProperty("规格值")
    private String propertyValue;
    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("销售价(个人价)")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;
    @ApiModelProperty("开始售卖时间")
    private LocalDateTime startSellTime;
    @ApiModelProperty("结束售卖时间")
    private LocalDateTime endSellTime;
    @ApiModelProperty("标签名称集合")
    private List<String> labelNames;
    @ApiModelProperty("V1.3.0 商品上下架状态")
    private Integer goodsUpDown;

    @ApiModelProperty(value = "菜单商品创建时间", hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty("V1.4.0 商品简介")
    private String goodsSynopsis;
    @ApiModelProperty("V1.4.0 商品添加方式 1-平台推送2-商家自创3-门店自创")
    private Integer goodsAddType;
    @ApiModelProperty(value = "V1.4.0 商品销售渠道 1.外卖小程序 2:食堂 3.全部")
    private Integer goodsSalesChannels;

    @ApiModelProperty(value = "商品规格类型")
    private Integer goodsSpecType;
    @ApiModelProperty(value = "商品打包费")
    private BigDecimal packPrice;

}
