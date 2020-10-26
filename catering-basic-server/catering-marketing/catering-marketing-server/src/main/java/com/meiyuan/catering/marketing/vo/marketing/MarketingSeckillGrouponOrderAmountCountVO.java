package com.meiyuan.catering.marketing.vo.marketing;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/08/05 16:08
 * @description 营销活动关联订单统计VO
 **/

@Data
public class MarketingSeckillGrouponOrderAmountCountVO {

    /**
     * 营销活动ID（包括秒杀/团购）
     */
    private Long ofId;
    /**
     * 营销活动类型（1：秒杀 2：团购）
     */
    private Integer ofType;
    /**
     * 订单金额统计
     */
    private BigDecimal count;

}
