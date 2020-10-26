package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/3/21 16:56
 * @description 简单描述
 **/
@Data
@ApiModel("商品id和名称")
public class GoodsNameAndIdDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("分类名称")
    private String categoryName;
    @ApiModelProperty(hidden = true)
    private Long goodsId;
}
