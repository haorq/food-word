package com.meiyuan.catering.marketing.vo.repertory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/08 17:08
 * @description 营销商品售出情况VO
 **/

@Data
@ApiModel(value = "营销商品售出情况统计VO")
public class MarketingRepertoryGoodsSoldVo {

    @ApiModelProperty(value = "营销商品ID")
    private Long mGoodsId;
    @ApiModelProperty(value = "营销商品售出数量统计")
    private Integer soldCount;
    @ApiModelProperty(value = "营销商品SKU规格值")
    private String goodsSkuCode;

}
