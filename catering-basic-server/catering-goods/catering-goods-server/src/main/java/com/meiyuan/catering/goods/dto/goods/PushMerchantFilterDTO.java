package com.meiyuan.catering.goods.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/3/25 11:25
 * @description 推送的商家过滤DTO
 **/
@Data
@ApiModel("推送的商家过滤数据模型")
public class PushMerchantFilterDTO {
    @ApiModelProperty("推送类型 1-商品 2-菜单")
    private Integer pushType;
    @ApiModelProperty("商品id")
    private Long goodsId;
    @ApiModelProperty("菜单id")
    private Long menuId;
    @ApiModelProperty("固定商家还是全部商家 1-所有商家 2-指定商家")
    private Integer fixedOrAll;

}
