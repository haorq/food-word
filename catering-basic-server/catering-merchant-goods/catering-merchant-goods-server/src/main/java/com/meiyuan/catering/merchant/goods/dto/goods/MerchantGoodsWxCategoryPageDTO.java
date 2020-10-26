package com.meiyuan.catering.merchant.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/2 11:46
 */
@Data
@ApiModel("小程序类目选择商品分页查询条件")
public class MerchantGoodsWxCategoryPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "门店id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "已选择的商家商品集合")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> shopGoodsIdList;
}
