package com.meiyuan.catering.merchant.dto.goods.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yaoozu
 * @description 商品分类
 * @date 2020/3/2117:55
 * @since v1.0.0
 */
@Data
@ApiModel("商品分类DTO")
public class MerchantGoodsCategoryResDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "类目名称")
    private String categoryName;
}
