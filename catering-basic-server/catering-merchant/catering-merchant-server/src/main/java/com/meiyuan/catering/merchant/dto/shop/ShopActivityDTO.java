package com.meiyuan.catering.merchant.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/6/11 0011 13:56
 * @Description 简单描述 : 店铺活动返回信息DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("店铺活动返回信息DTO")
public class ShopActivityDTO {
    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long value;
    @ApiModelProperty("店铺名称")
    private String label;
    @ApiModelProperty("门店负责人")
    private String primaryPersonName;
    @ApiModelProperty("联系方式")
    private String registerPhone;
    @ApiModelProperty("商户下已有活动的商品ID列表")
    private List<String> merchantGoodsItem;

}
