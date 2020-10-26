package com.meiyuan.catering.es.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/3/28 10:10
 * @description 简单描述
 **/
@Data
@ApiModel("商户商品查询参数模型")
public class EsMerchantGoodsQueryDTO {
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("售卖模式：1-菜单售卖模式 2-商品售卖模式")
    private Integer sellType;
    @ApiModelProperty("分类ID或者菜单ID")
    private Long categoryOrMenuId;
    @ApiModelProperty(value = "售卖状态：1-进行中；2-未开始；3-已结束",hidden = true)
    private Integer sellStatus;
}
