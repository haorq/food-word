package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/3/21 11:32
 * @description 商品推送商户数据DTO
 **/
@Data
@ApiModel("商品推送商户数据模型")
public class GoodsPushMerchantDTO {
    @ApiModelProperty("商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("固定商家还是全部商家 1-所有商家 2-指定商家")
    private Integer fixedOrAll;
    @ApiModelProperty("推送商户id集合")
    private List<Long> merchantIdList;
}
