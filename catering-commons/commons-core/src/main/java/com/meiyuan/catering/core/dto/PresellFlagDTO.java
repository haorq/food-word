package com.meiyuan.catering.core.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author zengzhangni
 * @date 2020/7/13 18:12
 * @since v1.1.0
 */
@Data
@Accessors(chain = true)
public class PresellFlagDTO {
    /**
     * 是否开启预售
     *
     * @since v1.2.0
     */
    Boolean presellFlag;
    /**
     * 预售开始时间
     *
     * @since v1.2.0
     */
    Date startSellTime;
    /**
     * 预售结束时间
     *
     * @since v1.2.0
     */
    Date endSellTime;
    /**
     * 星期 "1,5,6"
     *
     * @since v1.2.0
     */
    String sellWeekTime;
    /**
     * 截止下单时间
     *
     * @since v1.2.0
     */
    String closeSellTime;

    /**
     * 重新计算后的预售开始时间
     *
     * @since v1.2.0
     */
    Date newStartSellTime;
}
