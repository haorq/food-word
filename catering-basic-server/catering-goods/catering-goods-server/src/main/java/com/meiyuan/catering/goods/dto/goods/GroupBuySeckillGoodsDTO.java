package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/19 15:46
 * @description 团购秒杀商品DTO
 **/
@Data
public class GroupBuySeckillGoodsDTO {
    @ApiModelProperty("菜品名称")
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
    @ApiModelProperty("规格集合")
    private List<GoodsSkuDTO> skuList;
}
