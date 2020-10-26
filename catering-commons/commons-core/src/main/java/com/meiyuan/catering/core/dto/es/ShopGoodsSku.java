package com.meiyuan.catering.core.dto.es;

import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/8/6 16:33
 * @since v1.1.0
 */
@Data
public class ShopGoodsSku extends IdEntity {

    /**
     * 门店id
     */
    private Long shopId;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 门店商品表id
     */
    private Long shopGoodsSpuId;
    /**
     * 商品sku编码
     */
    private String skuCode;
    /**
     * 原价
     */
    private BigDecimal marketPrice;
    /**
     * 现价
     */
    private BigDecimal salesPrice;
    /**
     * 企业价
     */
    private BigDecimal enterprisePrice;
    /**
     * 外卖价
     */
    private BigDecimal takeoutPrice;
    /**
     * 打包费
     */
    private BigDecimal packPrice;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 规格值
     */
    private String propertyValue;
    /**
     * 每日剩余库存
     */
    private Integer remainStock;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
