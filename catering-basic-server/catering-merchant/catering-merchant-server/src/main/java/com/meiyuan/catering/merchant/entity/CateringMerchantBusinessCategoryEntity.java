package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 商户经营分类表(CateringMerchantBusinessCategory)实体类
 *
 * @author wxf
 * @since 2020-03-10 10:13:13
 */
@Data
@TableName("catering_merchant_business_category")
public class CateringMerchantBusinessCategoryEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = -77348371149679829L;
     /**
     * 商户id
     */
     @TableField(value = "merchant_id")
    private Long merchantId;
     /**
     * 分类id
     */
     @TableField(value = "category_id")
    private Long categoryId;
    }