package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author GongJunZheng
 * @date 2020/08/04 15:08
 * @description 平台秒杀活动场次与门店秒杀关系表实体V1.3.0
 **/

@Data
@TableName("catering_marketing_seckill_event_relation")
public class CateringMarketingSeckillEventRelationEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 5280974009938409749L;

    /**
     * 平台秒杀活动场次ID
     */
    @TableField(value = "event_id")
    private Long eventId;

    /**
     * 门店秒杀活动ID
     */
    @TableField(value = "seckill_id")
    private Long seckillId;

    /**
     * 营销商品主键ID
     */
    @TableField(value = "m_goods_id")
    private Long mGoodsId;

    /**
     * 场次时间（如：00:00-01:00）
     */
    @TableField(value = "event_time")
    private String eventTime;

    /**
     * 场次开始时间
     */
    @TableField(value = "event_begin_time")
    private LocalTime eventBeginTime;

    /**
     * 场次结束时间
     */
    @TableField(value = "event_end_time")
    private LocalTime eventEndTime;

    /**
     * 开始时间（同秒杀活动开始时间）
     */
    @TableField(value = "begin_time")
    private LocalDateTime beginTime;

    /**
     * 结束时间（同秒杀活动结束时间）
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

}
