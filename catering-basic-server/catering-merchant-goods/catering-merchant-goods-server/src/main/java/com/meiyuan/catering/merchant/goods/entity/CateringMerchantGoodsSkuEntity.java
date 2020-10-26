package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Data
@TableName("catering_merchant_goods_sku")
public class CateringMerchantGoodsSkuEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 9108945223503803004L;


    @TableField("merchant_goods_extend_id")
    private Long merchantGoodsExtendId;

    @TableField("goods_id")
    private Long goodsId;


    @TableField("spu_code")
    private String spuCode;

    @TableField("sku_code")
    private String skuCode;

    @TableField("property_value")
    private String propertyValue;

    @TableField("pic_url")
    private String picUrl;

    @TableField("market_price")
    private BigDecimal marketPrice;

    @TableField("sales_price")
    private BigDecimal salesPrice;

    @TableField("enterprise_price")
    private BigDecimal enterprisePrice;

    @TableField("pack_price")
    private BigDecimal packPrice;

    @TableField("sales_channels")
    private Integer salesChannels;

    @TableField("unit")
    private String unit;

    @TableField("stock")
    private Integer stock;

    @TableField("is_full_stock")
    private Boolean isFullStock;


    @TableField("remain_stock")
    private Integer remainStock;

    @TableField("discount_limit")
    private Integer discountLimit;

    @TableField("goods_spec_type")
    private Integer goodsSpecType;

    @TableField("update_time")
    private Date updateTime;

    @TableField("lowest_buy")
    private Integer lowestBuy;

    @TableField("highest_buy")
    private Integer highestBuy;

    @TableField("is_del")
    @TableLogic
    private Boolean del;

    @TableField("create_by")
    private Long createBy;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_by")
    private Long updateBy;

}
