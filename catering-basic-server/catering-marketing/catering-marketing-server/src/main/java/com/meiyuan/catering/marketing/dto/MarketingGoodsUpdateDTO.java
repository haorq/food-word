package com.meiyuan.catering.marketing.dto;

import com.meiyuan.catering.marketing.dto.marketing.MarketingEsGoodsUpdateDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MarketingGoodsUpdateDTO
 * @Description
 * @Author gz
 * @Date 2020/7/21 14:15
 * @Version 1.2.0
 */
@Data
public class MarketingGoodsUpdateDTO {
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 图片
     */
    private String listPicture;
    /**
     * 标签
     */
    private String listLabel;
    /**
     *
     */
    private List<MarketingEsGoodsUpdateDTO> newGoodsList;
    /**
     * sku信息  k-skuCode v-price
     */
    private Map<String, BigDecimal>  skuMap;
    /**
     * sku包装费 k-skuCode v-price
     */
    private Map<String, BigDecimal>  skuPackMap;
    /**
     * 商品描述
     */
    private String goodsDesc;

    /**
     * 商品简介
     */
    private String goodsSynopsis;

    /**
     * 商品门店价
     */
    private BigDecimal storePrice;
    /**
     * 门店ID
     */
    private Long shopId;
    /**
     * 商户ID
     */
    private Long merchantId;
    /**
     * 菜品分类id
     */
    private Long categoryId;
    /**
     * 菜品分类名称
     */
    private String categoryName;
    /**
     * 修改方 1-平台端  2-其他端
     */
    private Integer editType;
}
