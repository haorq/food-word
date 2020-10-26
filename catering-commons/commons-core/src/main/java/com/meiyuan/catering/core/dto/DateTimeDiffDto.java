package com.meiyuan.catering.core.dto;

import lombok.Data;

/**
 * @author lh
 */
@Data
public class DateTimeDiffDto {
    private long day;
    private long hour;
    private long minute;
    /**
     * 总分钟数
     */
    private long minuteTotal;
    private long seconds;
}
