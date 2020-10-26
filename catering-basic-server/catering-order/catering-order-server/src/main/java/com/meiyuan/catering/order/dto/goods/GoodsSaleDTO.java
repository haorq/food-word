package com.meiyuan.catering.order.dto.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class GoodsSaleDTO {

    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品分类id")
    private Long goodsGroupId;
    @ApiModelProperty("商品分类名称")
    private String goodsGroupName;
    @ApiModelProperty("商品销量")
    private Integer goodsSaleNum;
    @ApiModelProperty("商品销售额")
    private BigDecimal goodsSaleAmout;
    @ApiModelProperty("优惠总额")
    private BigDecimal totalDiscountFee;
    @ApiModelProperty("优惠后总额")
    private BigDecimal afterDiscountFee;
    @ApiModelProperty("销售占比")
    private BigDecimal salePercentage;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    private String goodsSkuCode;

    public BigDecimal getTotalDiscountFee(){
        return this.goodsSaleAmout.subtract(this.afterDiscountFee);
    }


}
