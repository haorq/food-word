package com.meiyuan.catering.order.dto.query;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class GoodsSalePageParamDTO extends BasePageDTO {

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty("商品分类")
    private String goodsType;
    @ApiModelProperty("商品分类id")
    private Long goodsGroupId;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("门店名称")
    private String shopName;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty(value = "排序字段")
    private String orderField;
    /**
     * 排除商品sku
     */
    private List<String> exlGoodsSku;
}
