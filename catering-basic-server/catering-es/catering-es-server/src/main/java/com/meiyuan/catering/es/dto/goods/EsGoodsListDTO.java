package com.meiyuan.catering.es.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.es.dto.merchant.EsMerchantBaseInfoDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/28 11:24
 * @description 简单描述
 **/
@Data
@ApiModel("商品列表模型")
public class EsGoodsListDTO extends EsMerchantBaseInfoDTO {
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("名称")
    private String goodsName;
    @ApiModelProperty("菜品分类id")
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("分类商品排序号")
    private Integer categoryGoodsSort;
    @ApiModelProperty("列表图片")
    private String listPicture;
    @ApiModelProperty("详情图片")
    private String infoPicture;
    @ApiModelProperty("月销量")
    private Long monthSalesCount;
    @ApiModelProperty("商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("商品描述")
    private String goodsDescribeText;


    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("现价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "每单限x份优惠")
    private Integer discountLimit;

    @ApiModelProperty("是否为预售")
    private Boolean presellFlag;
    @ApiModelProperty("开始售卖时间")
    private Date startSellTime;
    @ApiModelProperty("结束售卖时间")
    private Date endSellTime;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("星期（1-7）")
    private String sellWeekTime;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("当天截止售卖商家 HH:mm")
    private String closeSellTime;

    @ApiModelProperty("标签集合")
    private List<EsGoodsCategoryAndLabelDTO> labelList;
    @ApiModelProperty("规格集合")
    private List<EsGoodsSkuDTO> skuList;
}
