package com.meiyuan.catering.es.dto.marketing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.es.dto.merchant.EsMerchantBaseInfoDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName MarketingListDTO
 * @Description 促销活动列表接口
 * @Author gz
 * @Date 2020/3/27 18:02
 * @Version 1.1
 */
@Data
public class EsMarketingListDTO extends EsMerchantBaseInfoDTO {
    @ApiModelProperty(value = "活动id 非唯一 ES中不作为ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动对象：0-全部；1-个人；2-企业")
    private Integer objectLimit;
    @ApiModelProperty(value = "商家Logo")
    private String merchantLogo;
    /**
     * 活动类型:1-秒杀；2-拼团；3-团购；4-优惠券；
     */
    @ApiModelProperty(value = "活动类型:1-秒杀；2-拼团；3-团购；4-优惠券；")
    private Integer ofType;
    /**
     * 活动商品表主键ID--活动下唯一
     */
    @ApiModelProperty(value = "活动商品表主键ID ES中作为ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long mGoodsId;
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品市场价")
    private BigDecimal storePrice;

    @ApiModelProperty(value = "商品图片-列表图")
    private String goodsPicture;
    /**
     * 商品sku
     */
    @ApiModelProperty(value = "商品sku")
    private String sku;
    @ApiModelProperty(value = "规格值")
    private String skuValue;
    @ApiModelProperty(value = "个人-起购数量")
    private Integer minQuantity;
    /**
     * 商品数量/发行数量--库存
     */
    @ApiModelProperty(value = "商品数量/发行数量-总库存")
    private Integer quantity;
    /**
     * 活动价
     */
    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;

    @ApiModelProperty(value = "已售数量")
    private Integer soldOut;
    @ApiModelProperty(value = "剩余库存")
    private Integer residualInventory;
    @ApiModelProperty(value = "状态：1-进行中；2-未开始；3-已结束")
    private Integer status;
    @ApiModelProperty("售卖模式 1-菜单 2-商品")
    private Integer sellType;
    @ApiModelProperty(value = "起团数量")
    private Integer minGrouponQuantity;
    @ApiModelProperty(value = "限购数量")
    private Integer limitQuantity;
    @ApiModelProperty(value = "规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty(value = "餐盒费")
    private double packPrice;

    public String getGoodsPicture(){
        if(StringUtils.isNotBlank(this.goodsPicture)){
            return StringUtils.substringBefore(this.goodsPicture,",");
        }
        return StringUtils.EMPTY;
    }

}
