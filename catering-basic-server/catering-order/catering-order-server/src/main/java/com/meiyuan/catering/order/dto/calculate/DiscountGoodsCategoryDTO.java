package com.meiyuan.catering.order.dto.calculate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 功能描述:  优惠卷关联商品分类
 * @author: XiJie Xie
 * @date: 2020/3/27 15:13
 * @version v 3.5
 */
@Data
@ToString(callSuper = true)
public class DiscountGoodsCategoryDTO {
    @ApiModelProperty(value = "商品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsCategoryId;
    @ApiModelProperty(value = "商品分类名称")
    private String goodsCategoryName;
}
