package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Data
@TableName("catering_shop_goods_sku")
public class CateringShopGoodsSkuEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 6274834065239960370L;
    /**
     *  门店id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 门店商品表id
     */
    @TableField("shop_goods_spu_id")
    private Long shopGoodsSpuId;
    /**
     * 商品sku编码
     */
    @TableField("sku_code")
    private String skuCode;
    /**
     * 原价
     */
    @TableField("market_price")
    private BigDecimal marketPrice;
    /**
     * 现价
     */
    @TableField("sales_price")
    private BigDecimal salesPrice;
    /**
     * 企业价
     */
    @TableField("enterprise_price")
    private BigDecimal enterprisePrice;
    /**
     * 外卖价
     */
    @TableField("takeout_price")
    private BigDecimal takeoutPrice;
    /**
     * 打包费
     */
    @TableField("pack_price")
    private BigDecimal packPrice;
    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
    /**
     * 规格值
     */
    @TableField("property_value")
    private String propertyValue;
    /**
     * 每日剩余库存
     */
    @TableField("remain_stock")
    private Integer remainStock;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("is_del")
    @TableLogic
    private Boolean del;
}
