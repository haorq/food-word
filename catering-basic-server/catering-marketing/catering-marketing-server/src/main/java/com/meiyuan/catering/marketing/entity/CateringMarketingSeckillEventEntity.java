package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author GongJunZheng
 * @date 2020/08/04 15:08
 * @description 平台秒杀活动场次表实体V1.3.0
 **/

@Data
@TableName("catering_marketing_seckill_event")
public class CateringMarketingSeckillEventEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 3100704125352788792L;

    /**
     * 开始时间
     */
    @TableField(value = "begin_time")
    private LocalTime beginTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private LocalTime endTime;

    /**
     * 是否被删除（0：否 1：是）,默认为0
     */
    @TableField(value = "is_del")
    @TableLogic
    private Boolean del;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @TableField(value = "update_by")
    private Long updateBy;

}
