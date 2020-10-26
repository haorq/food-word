package com.meiyuan.catering.es.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/3/28 13:43
 * @description 简单描述
 **/
@Data
@ApiModel("商户商品模型")
public class EsMerchantGoodsDTO {
    @ApiModelProperty("分类列表")
    private List<EsGoodsCategoryAndLabelDTO> categoryList;
    @ApiModelProperty("商品列表")
    private List<EsGoodsListDTO> goodsList;
}
