package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 商品上下架
 * @date 2020/5/2211:55
 * @since v1.0.0
 */
@Data
public class GoodsUpOrDownDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(hidden = true)
    private Long shopId;
    @ApiModelProperty("商品上下架 1下架 2上架")
    private Integer upOrDownStatus;
}
