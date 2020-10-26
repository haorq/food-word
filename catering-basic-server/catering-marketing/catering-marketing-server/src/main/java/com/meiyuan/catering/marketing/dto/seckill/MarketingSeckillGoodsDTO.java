package com.meiyuan.catering.marketing.dto.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/08/06 10:08
 * @description 秒杀商品信息DTO
 **/

@Data
@ApiModel(value = "秒杀商品信息DTO")
public class MarketingSeckillGoodsDTO {

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;
    /**
     * 商品sku
     */
    @ApiModelProperty(value = "商品sku")
    @NotBlank(message = "商品SKU不能为空")
    private String skuCode;
    /**
     * 原价
     */
    @ApiModelProperty(value = "原价")
    private BigDecimal storePrice;
    /**
     * 活动价
     */
    @ApiModelProperty(value = "活动价")
    @NotNull(message = "活动价不能为空")
    private BigDecimal activityPrice;
    /**
     * 秒杀总数量
     */
    @ApiModelProperty(value = "每场秒杀总数量")
    @NotNull(message = "每场秒杀总数量不能为空")
    @Range(min = 1, max = 999999, message = "每场秒杀总数量值错误")
    private Integer quantity;
    /**
     * 起购数量
     */
    @ApiModelProperty(value = "最低购买数量")
    private Integer minQuantity;
    /**
     * 限购数量
     */
    @ApiModelProperty(value = "最高购买数量")
    private Integer limitQuantity;

    @ApiModelProperty(value = "商品排序序号", hidden = true)
    private Integer goodsSort;

    public Integer getMinQuantity() {
        if(null == minQuantity) {
            minQuantity = 1;
        }
        return minQuantity;
    }

    public Integer getLimitQuantity() {
        if(null == limitQuantity) {
            limitQuantity = -1;
        }
        return limitQuantity;
    }
}
