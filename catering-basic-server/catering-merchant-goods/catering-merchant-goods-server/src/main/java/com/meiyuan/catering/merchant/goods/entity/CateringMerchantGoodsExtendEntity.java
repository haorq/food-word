package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Data
@TableName("catering_merchant_goods_extend")
public class CateringMerchantGoodsExtendEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = -6208864266113239040L;


    /**
     *  商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 商户id/加工厂id
     */
    @TableField("merchant_id")
    private Long merchantId;
    /**
     * 门店id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 商户商品名称
     */
    @TableField("merchant_goods_name")
    private String merchantGoodsName;
    /**
     * 分类id
     */
    @TableField("category_id")
    private Long categoryId;
    /**
     * 分类名称
     */
    @TableField("category_name")
    private String categoryName;
    /**
     * 1-下架,2-上架
     */
    @TableField("merchant_goods_status")
    private Integer merchantGoodsStatus;
    /**
     * 是否预售0-否 1-是
     */
    @TableField("presell_flag")
    private Boolean presellFlag;
    /**
     * 开始售卖时间（yyyy-mm-dd）
     */
    @TableField(value = "start_sell_time",updateStrategy = FieldStrategy.IGNORED)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startSellTime;
    /**
     * 结束售卖时间（yyyy-mm-dd）
     */
    @TableField(value = "end_sell_time",updateStrategy = FieldStrategy.IGNORED)
    private LocalDate endSellTime;
    /**
     * 星期一到天（1-7）
     */
    @TableField(value = "sell_week_time",updateStrategy = FieldStrategy.IGNORED)
    private String sellWeekTime;
    /**
     * 当天截止售卖时间（hh:ss）
     */
    @TableField(value = "close_sell_time",updateStrategy = FieldStrategy.IGNORED)
    private String closeSellTime;
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
     * 企业价（多个企业价最低的那个）
     */
    @TableField("enterprise_price")
    private BigDecimal enterprisePrice;
    /**
     * 商品权重
     */
    @TableField("goods_weight")
    private Long goodsWeight;
    /**
     * 商品单位，例如件、盒( 天，月，年)
     */
    @TableField("unit")
    private String unit;
    /**
     * 1-统一规格 2-多规格
     */
    @TableField("goods_spec_type")
    private Integer goodsSpecType;
    /**
     * 最多购买 -1表示无限制
     */
    @TableField("highest_buy")
    private Integer highestBuy;
    /**
     * 最低购买
     */
    @TableField("lowest_buy")
    private Integer lowestBuy;
    /**
     * 1-平台推送2-商家自创
     */
    @TableField("goods_add_type")
    private Integer goodsAddType;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 0-否 1-是
     */
    @TableField("is_del")
    @TableLogic
    private Boolean del;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

}
