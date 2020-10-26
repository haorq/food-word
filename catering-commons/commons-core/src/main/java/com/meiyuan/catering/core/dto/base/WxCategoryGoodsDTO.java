package com.meiyuan.catering.core.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/5/6
 */
@Data
public class WxCategoryGoodsDTO implements Serializable {
    private static final long serialVersionUID = -202009021417110501L;

    @ApiModelProperty("商品id")
    private String goodsId;

    @ApiModelProperty(value = "门店商品规格(SPU)id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopGoodsId;

    @ApiModelProperty(value = "门店id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty("商品名称")
    private String goodsName;
}
