package com.meiyuan.catering.merchant.goods.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/10 14:08
 * @description V1.3.0促销活动模块选择商品DTO
 **/

@Data
@ApiModel(value = "V1.3.0促销活动模块选择商品DTO")
public class MarketingGoodsSelectDTO {

    @ApiModelProperty("菜品名称/编码")
    private String goodsName;
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("团购秒杀查询商品的参数 - 不包含的商品SKU的编码集合")
    private List<String> goodsSkuList;
    @ApiModelProperty(value = "店铺id", hidden = true)
    private Long shopId;

}
