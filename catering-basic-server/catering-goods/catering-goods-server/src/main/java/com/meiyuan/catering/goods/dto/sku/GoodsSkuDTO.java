package com.meiyuan.catering.goods.dto.sku;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wxf
 * @date 2020/3/16 11:33
 * @description 新增/查看商品SKU DTO
 **/
@Data
@ApiModel("新增/查看商品SKU模型")
public class GoodsSkuDTO {
    @ApiModelProperty("id 新增不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("商品id 可以不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("sku编码 新增不传")
    private String skuCode;
    @ApiModelProperty("规格值")
    private String propertyValue;
    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("销售价(个人价)")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;
    @ApiModelProperty("最高购买限制 1-无限制 2-自定义")
    private Integer buyLimitType;
    @ApiModelProperty("最低购买")
    private Integer lowestBuy;
    @ApiModelProperty("最多购买,-1表示无限制")
    private Integer highestBuy;
    @ApiModelProperty(hidden = true, value = "false-没有删除 true-删除")
    private Boolean del;
    @ApiModelProperty("1.外卖小程序 2:食堂 3.全部")
    private Integer salesChannels;
    @ApiModelProperty("商品单位，例如件、盒( 天，月，年)")
    private String unit;
    @ApiModelProperty("每日库存")
    private Integer stock;
    @ApiModelProperty("0-false 1-true")
    private Boolean isFullStock;
    /**
     * 每单限制优惠 不传默认每单限制0
     */
    @ApiModelProperty("每单限制优惠")
    private Integer discountLimit;
    @ApiModelProperty(value = "1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty(value = "打包费")
    private BigDecimal packPrice;

    public GoodsSkuDTO() {
        this.buyLimitType = 1;
        this.highestBuy = -1;
        this.discountLimit = 0;
        this.salesPrice= BigDecimal.valueOf(-1);
        this.enterprisePrice= BigDecimal.valueOf(-1);
        this.packPrice= BigDecimal.valueOf(-1);
    }
}
