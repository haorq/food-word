package com.meiyuan.catering.merchant.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MarketingSelectGoodsQueryDTO
 * @Description 促销活动选择商品 查询DTO
 * @Author gz
 * @Date 2020/7/10 14:43
 * @Version 1.2.0
 */
@Data
public class MarketingSelectGoodsQueryDTO {
    @ApiModelProperty("菜品名称/编码")
    private String goodsNameCode;
    @ApiModelProperty("分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty("团购秒杀查询商品的参数 - 不包含的商品id集合")
    private List<Long> goodsIdList;
    @ApiModelProperty("团购秒杀查询商品的参数 - 商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

}
