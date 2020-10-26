package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/21 19:06
 * @description 简单商品DTO
 **/
@Data
@ApiModel("简单商品数据模型")
public class SimpleGoodsDTO extends GoodsNameAndIdDTO{
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("商品id")
    private Long id;
    @ApiModelProperty("市场价(原价)")
    private BigDecimal marketPrice;
    @ApiModelProperty("销售价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;
    @ApiModelProperty(hidden = true)
    private String listPicture;
    @ApiModelProperty(hidden = true)
    private Long merchantId;
    @ApiModelProperty(hidden = true)
    private List<GoodsCategoryAndLabelDTO> labelList;

}
