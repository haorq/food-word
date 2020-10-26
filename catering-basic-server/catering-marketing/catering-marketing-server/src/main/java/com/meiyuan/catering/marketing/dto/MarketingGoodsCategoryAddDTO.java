package com.meiyuan.catering.marketing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName MarketingGoodsCategoryDTO
 * @Description 商品系列DTO
 * @Author gz
 * @Date 2020/3/18 18:45
 * @Version 1.1
 */
@Data
public class MarketingGoodsCategoryAddDTO {
    @ApiModelProperty(value = "商品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsCategoryId;
    @ApiModelProperty(value = "商品分类名称")
    private String goodsCategoryName;
}
