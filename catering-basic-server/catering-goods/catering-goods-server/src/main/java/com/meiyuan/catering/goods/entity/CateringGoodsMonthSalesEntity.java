package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author wxf
 * @date 2020/4/8 16:46
 * @description 商品月销
 **/
@Data
@TableName("catering_goods_month_sales")
public class CateringGoodsMonthSalesEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 262753099085737779L;
    /**
     * 时间-年月日
     */
    @TableField(value = "time")
    private LocalDate time;
    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;
    /**
     * sku_code
     */
    @TableField(value = "sku_code")
    private String skuCode;
    /**
     * 数量
     */
    @TableField(value = "number")
    private Long number;
    /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    private Long shopId;
}
