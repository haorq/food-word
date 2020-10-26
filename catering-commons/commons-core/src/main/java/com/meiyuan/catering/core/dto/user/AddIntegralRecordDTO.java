package com.meiyuan.catering.core.dto.user;

import lombok.Data;

/**
 * @ClassName AddIntegralRecordDTO
 * @Description
 * @Author gz
 * @Date 2020/8/20 14:47
 * @Version 1.3.0
 */
@Data
public class AddIntegralRecordDTO {

    private Long id;
    private Long activityId;
    private Long activityRuleId;
    private Integer activityType;
    private Long userId;
    private Integer getNum;
    private Integer rewardType;
}
