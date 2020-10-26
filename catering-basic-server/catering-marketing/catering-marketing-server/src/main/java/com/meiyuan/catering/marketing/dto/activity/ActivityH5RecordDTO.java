package com.meiyuan.catering.marketing.dto.activity;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName ActivityH5RecordDTO
 * @Description
 * @Author gz
 * @Date 2020/10/9 11:09
 * @Version 1.5.0
 */
@Data
@Builder
public class ActivityH5RecordDTO {

    private Long id;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 是否领取
     */
    private Boolean get;
    /**
     * 优惠券ids
     */
    private String ticketIds;
}
