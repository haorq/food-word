package com.meiyuan.catering.marketing.dto.seckill;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName SeckillMsgBodyDTO
 * @Description 秒杀消息Body DTO
 * @Author gz
 * @Date 2020/3/26 12:37
 * @Version 1.1
 */
@Data
@Builder
public class SeckillMsgBodyDTO implements Serializable {
    /** 秒杀商品主键id */
    private Long seckillGoodsId;
    /** 用户id */
    private Long userId;
    /** 用户类型 */
    private Integer userType;
    /** 秒杀数量 */
    private Integer number;
    /** 商家id */
    private Long merchantId;
    /**
     * 活动价
     */
    private BigDecimal activityPrice;
}
