package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/10/13 11:10
 * @description 商家负债表实体类
 **/

@Data
@TableName("catering_orders_shop_debt")
public class CateringOrdersShopDebtEntity extends IdEntity implements Serializable {

    /**
     * 商户ID
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 负债总金额
     */
    @TableField("amount")
    private BigDecimal amount;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

}
