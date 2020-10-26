package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 平台授权商户商品表(CateringPlatformEmpowerMerchantGoods)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:39:54
 */
@Data
@TableName("catering_platform_empower_merchant_goods")
public class CateringPlatformEmpowerMerchantGoodsEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 655809854866458090L;
    /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;
    /**
     * 数量
     */
    @TableField(value = "number")
    private Long number;
    /**
     * 逻辑删除 0-没有删除 1-删除
     */
    @TableField(value = "is_del")
    private Boolean del;
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_by")
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}