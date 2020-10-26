package com.meiyuan.catering.es.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author GongJunZheng
 * @date 2020/08/27 09:08
 * @description 团购商品详情DTO
 **/

@Data
@ApiModel(value = "团购商品详情DTO")
public class EsMarketingGrouponGoodsDetailDTO {

    @ApiModelProperty(value = "营销团购商品ID")
    @NotNull(message = "营销团购商品ID不能为空")
    private Long mGoodsId;
    @ApiModelProperty(value = "门店ID")
    @NotNull(message = "门店ID不能为空")
    private Long shopId;

}
