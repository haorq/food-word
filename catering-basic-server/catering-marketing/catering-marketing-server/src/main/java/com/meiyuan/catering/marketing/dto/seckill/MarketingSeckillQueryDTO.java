package com.meiyuan.catering.marketing.dto.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author yaoozu
 * @description 秒杀活动查询DTO
 * @date 2020/6/111:37
 * @since v1.1.0
 */
@Data
public class MarketingSeckillQueryDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    /**
     * 活动对象 2-企业、1-个人
     */
    private Integer objectLimit;
}
