package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wxf
 * @date 2020/3/23 13:43
 * @description 商品分类/标签信息
 **/
@Data
@ApiModel("商品分类/标签信息模型")
public class GoodsCategoryAndLabelDTO {
    @ApiModelProperty("分类还是标签 1-分类 2-标签")
    private Integer type;
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("商品id")
    private Long goodsId;
    @ApiModelProperty("商品排序号")
    private Integer sort;
}
