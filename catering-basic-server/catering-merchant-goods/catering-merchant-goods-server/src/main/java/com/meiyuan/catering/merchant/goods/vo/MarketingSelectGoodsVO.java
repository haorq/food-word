package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MarketingSelectGoodsVO
 * @Description 促销活动模块选择商品VO
 * @Author gz
 * @Date 2020/7/10 14:36
 * @Version 1.2.0
 */
@Data
public class MarketingSelectGoodsVO {
    @ApiModelProperty("菜品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("菜品名称")
    private String goodsName;
    @ApiModelProperty("菜品编号")
    private String spuCode;
    @ApiModelProperty("菜品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("商品单位，例如件、盒 V1.3.0")
    private String unit;
    @ApiModelProperty("规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("规格集合")
    private List<MarketingSelectGoodsSkuVO> skuList;
}
