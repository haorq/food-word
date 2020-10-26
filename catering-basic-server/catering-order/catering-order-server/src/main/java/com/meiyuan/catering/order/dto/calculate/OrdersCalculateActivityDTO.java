package com.meiyuan.catering.order.dto.calculate;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  订单结算活动信息
 *
 * @author xie-xi-jie
 * @since 2020-03-26 18:04:06
 */
@Data
@ToString(callSuper = true)
public class OrdersCalculateActivityDTO implements Serializable {
    private static final long serialVersionUID = -46028568534655719L;

    /** 关联维度（1：订单维度关联；2：订单商品维度关联） */
    private Integer relationDimension;
    /** 营销活动类型（1：秒杀；2：团购；3：拼单，4：特价） */
    private Integer activityType;
    /** 营销活动ID */
    private Long activityId;
    /** 营销活动编号 */
    private String activityNo;
    /** 营销活动名称 */
    private String activityName;
    /** 营销活动是否限制时间（1：有时间限制；0：无时间限制） */
    private Integer activityTimeLimited;
    /** 营销活动开始时间 */
    private LocalDateTime activityBeginTime;
    /** 营销活动截止时间 */
    private LocalDateTime activityEndTime;

}
