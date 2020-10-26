package com.meiyuan.catering.marketing.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName MarketingGoodsTransferDTO
 * @Description 商品数据转换DTO
 * @Author gz
 * @Date 2020/3/20 10:26
 * @Version 1.1
 */
@Data
public class MarketingGoodsTransferDTO {
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * Code编码
     */
    private String code;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品类型
     */
    private Integer goodsType;
    /**
     * 商品sku
     */
    private String sku;
    /**
     * 商品数量/发行数量
     */
    private Integer quantity;
    /**
     * 限购数量
     */
    private Integer limitQuantity;
    /**
     * 起购数量
     */
    private Integer minQuantity;
    /**
     * 活动价
     */
    private BigDecimal activityPrice;
    /**
     * 市场价
     */
    private BigDecimal storePrice;
    /**
     * 团购价
     */
    private BigDecimal grouponPrice;

    /**
     * 起团数量
     */
    private Integer minGrouponQuantity;
    /**
     * 商品图片
     */
    private String goodsPicture;
    /**
     * 商品详细介绍
     */
    private String goodsDesc;
    /**
     * 规格值
     */
    private String skuValue;
    /**
     * 菜品分类id
     */
    private Long categoryId;
    /**
     * 菜品分类名称
     */
    private String categoryName;
    /**
     * 商品标签名称集合
     */
    private List<String> labelList;
    /**
     * V1.3.0 商品上下架状态
     */
    private Integer goodsUpDown;
    /**
     * V1.3.0 商品排序
     */
    private Integer goodsSort;
    /**
     * V1.3.0 菜单商品创建时间
     */
    private LocalDateTime createTime;
    /**
     * 商品简介
     */
    private String goodsSynopsis;
    /**
     * V1.4.0 商品添加方式 1-平台推送2-商家自创3-门店自创
     */
    private Integer goodsAddType;
    /**
     * V1.4.0 商品销售渠道 1.外卖小程序 2:食堂 3.全部
     */
    private Integer goodsSalesChannels;
    /**
     * V1.5.0 商品规格类型
     */
    private Integer goodsSpecType;
    /**
     * V1.5.0 商品打包费
     */
    private BigDecimal packPrice;
}
