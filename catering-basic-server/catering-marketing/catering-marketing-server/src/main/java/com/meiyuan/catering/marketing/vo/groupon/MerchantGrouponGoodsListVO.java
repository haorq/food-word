package com.meiyuan.catering.marketing.vo.groupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author luohuan
 * @date 2020/3/18
 **/
@Data
@ApiModel("团购包含的商品VO")
@NoArgsConstructor
public class MerchantGrouponGoodsListVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("商品ID")
    private Long goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("销售价")
    private BigDecimal marketPrice;

    @ApiModelProperty("最低购买数量")
    private Integer minQuantity;

    @ApiModelProperty("活动价格")
    private BigDecimal activityPrice;

    @ApiModelProperty("商品规格值")
    private String propertyValue;

    @ApiModelProperty("起团数量")
    private Integer minGroupQuantity;

    public MerchantGrouponGoodsListVO(CateringMarketingGoodsEntity goodsEntity) {
        this.setGoodsId(goodsEntity.getGoodsId());
        this.setGoodsName(goodsEntity.getGoodsName());
        this.setActivityPrice(goodsEntity.getActivityPrice());
        this.setMinQuantity(goodsEntity.getMinQuantity());
        this.setMarketPrice(goodsEntity.getStorePrice());
        this.setPropertyValue(goodsEntity.getSkuValue());
        this.setMinGroupQuantity(goodsEntity.getMinGrouponQuantity());
    }
}
