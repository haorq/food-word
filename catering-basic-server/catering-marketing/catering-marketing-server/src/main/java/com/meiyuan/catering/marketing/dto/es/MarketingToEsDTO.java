package com.meiyuan.catering.marketing.dto.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName MarketingEsDTO
 * @Description 促销EsDTO
 * @Author gz
 * @Date 2020/3/24 19:31
 * @Version 1.1
 */
@Data
public class MarketingToEsDTO {
    @ApiModelProperty(value = "活动id 非唯一 ES中不作为ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动对象：0-全部；1-个人；2-企业")
    private Integer objectLimit;
    @ApiModelProperty(value = "商家id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "商家名称")
    private String merchantName;
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
     * 活动类型:1-秒杀；2-拼团；3-团购；4-优惠券；
     */
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

    @ApiModelProperty("商品详细介绍")
    private String goodsDescribeText;
    /**
     * 商品标签
     */
    @ApiModelProperty(value = "商品标签JSON",hidden = true)
    private String goodsLabel;
    /**
     * 商品sku
     */
    @ApiModelProperty(value = "商品sku")
    private String sku;
    /**
     * 商品数量/发行数量--库存
     */
    @ApiModelProperty(value = "商品数量/发行数量")
    private Integer quantity;
    /**
     * 限购数量
     */
    @ApiModelProperty(value = "个人-限购数量")
    private Integer limitQuantity;
    @ApiModelProperty(value = "个人-起购数量")
    private Integer minQuantity;
    /**
     * 起团数量
     */
    @ApiModelProperty(value = "起团数量")
    private Integer minGrouponQuantity;
    /**
     * 活动价
     */
    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;

    @ApiModelProperty(value = "规格值")
    private String skuValue;
    @ApiModelProperty(value = "已售数量")
    private Integer soldOut;
    @ApiModelProperty(value = "剩余库存")
    private Integer residualInventory;
    /**
     * 1-下架 2-上架
     */
    @ApiModelProperty(value = "上下架状态")
    private Integer upDownState;
    @ApiModelProperty(value = "删除标记")
    private Boolean del;

    @ApiModelProperty(value = "V1.3.0 商品上下架状态")
    private Integer goodsUpDownState;
    @ApiModelProperty(value = "V1.3.0 商品排序")
    private Integer goodsSort;
    @ApiModelProperty(value = "V1.3.0 商户状态 1-启用 2-禁用")
    private Integer merchantState;
    @ApiModelProperty(value = "V1.3.0 门店状态 1-启用 2-禁用")
    private Integer shopState;
    @ApiModelProperty(value = "V1.3.0 门店服务类型 3: wx、堂食都可展示 ，2：堂食展示 ，1：wx端展示")
    private Integer shopServiceType;
    @ApiModelProperty(value = "V1.4.0 秒杀场次信息（优化）")
    private String seckillEventIds;
    @ApiModelProperty(value = "V1.4.0 商品简介")
    private String goodsSynopsis;
    @ApiModelProperty(value = "V1.4.0 商品添加方式 1-平台推送2-商家自创3-门店自创")
    private Integer goodsAddType;
    @ApiModelProperty(value = "V1.4.0 商品销售渠道 1.外卖小程序 2:食堂 3.全部")
    private Integer goodsSalesChannels;
    @ApiModelProperty(value = "V1.5.0 商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty(value = "V1.5.0 商品打包费")
    private BigDecimal packPrice;

    public MarketingToEsDTO() {
        this.packPrice = new BigDecimal("-1");
    }

}
