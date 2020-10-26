package com.meiyuan.catering.merchant.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺配送自提时间范围表
 *
 * @author meitao
 * @since 2020-03-16 11:50:43
 */
@Data
@TableName("catering_shop_delivery_time_range")
public class CateringShopDeliveryTimeRangeEntity extends IdEntity implements Serializable {

    /**类型:1：店铺配送时间范围，2：店铺自提时间范围*/
   @TableField(value = "type")
    private Integer type;
    /**店铺id*/
   @TableField(value = "shop_id")
    private Long shopId;
    /**开始时间*/   
   @TableField(value = "start_time")
    private String startTime;
    /**结束时间*/   
   @TableField(value = "end_time")
    private String endTime;
    /**
     * 0-否 1-是
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    private Boolean isDel;
    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}