package com.meiyuan.catering.goods.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/4/8 11:23
 * @description 简单描述
 **/
@Data
@ApiModel("商户商品名称查询数据模型")
public class MerchantGoodsNameQueryDTO {
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("类型：3：全部，2：出售中，1：已下架")
    private Integer type;
    @ApiModelProperty("商户id(此时为门店id)")
    private String merchantId;
}
