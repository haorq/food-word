package com.meiyuan.catering.core.dto.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Description 时间范围
 * @Date  2020/3/24 0024 14:03
 */
@Data
public class TimeRangeDTO implements Serializable {
    private static final long serialVersionUID = -4330603343019914584L;
    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
