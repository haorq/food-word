package com.meiyuan.catering.merchant.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商户商品库存表(CateringMerchantGoodsStock)表实体类
 *
 * @author meitao
 * @since 2020-03-16 11:46:31
 */
@Data
@TableName("catering_merchant_goods_stock")
public class CateringMerchantGoodsStockEntity extends IdEntity implements Serializable {
  
    /**id*/   
   @TableField(value = "id")
    private Long id;
    /**商户id*/   
   @TableField(value = "merchant_id")
    private Long merchantId;
    /**门店id*/   
   @TableField(value = "shop_id")
    private Long shopId;
    /**商品id*/   
   @TableField(value = "goods_id")
    private Long goodsId;
    /**商品sku编码*/   
   @TableField(value = "sku_code")
    private String skuCode;
    /**库存*/   
   @TableField(value = "shop_stock")
    private Long shopStock;
    /**是否删除*/   
   @TableField(value = "is_del")
    private Boolean isDel;
    /**创建人*/   
   @TableField(value = "create_by")
    private Long createBy;
    /**修改人*/   
   @TableField(value = "update_by")
    private Long updateBy;
    /**创建时间*/   
   @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**修改时间*/   
   @TableField(value = "update_time")
    private LocalDateTime updateTime;


}