package com.meiyuan.catering.core.dto.goods;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MerchantGoodsToEsDTO
 * @Description 商户商品信息变更同步ES DTO
 * @Author gz
 * @Date 2020/7/16 9:38
 * @Version 1.2.0
 */
@Data
public class MerchantGoodsToEsDTO {
    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 商户分类id
     */
    private String categoryId;
    /**
     * 商户分类名称
     */
    private String categoryName;
    /**
     * 商户默认分类id --- 默认分类id有值则设置为默认分类id和名称
     */
    private String defaultCategoryId;
    /**
     * 商户默认分类名称
     */
    private String defaultCategoryName;

    /**
     * 门店id
     */
    private String shopId;
    /**
     * 商品对应的分类排序号
     */
    private List<GoodsSort> sorts;
}
