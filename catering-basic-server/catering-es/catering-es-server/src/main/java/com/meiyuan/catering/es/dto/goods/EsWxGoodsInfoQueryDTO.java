package com.meiyuan.catering.es.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/4/2 11:21
 * @description 简单描述
 **/
@Data
@ApiModel("微信商品详情信息查询数据模型")
public class EsWxGoodsInfoQueryDTO {
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("商品类型 1-菜单 2-商品 3-拼单 4-秒杀")
    private Integer goodsType;
    @ApiModelProperty("菜单ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
    @ApiModelProperty("拼单号")
    private String shareBillNo;
    @ApiModelProperty("门店id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
}
