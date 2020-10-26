package com.meiyuan.catering.user.dto.cart;

import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/8/13 14:50
 * @since v1.1.0
 */
@Data
public class CartSeckillNumDTO {

    private Long userId;
    private Long merchantId;
    private Long shopId;
    private Long seckillEventId;
    private Long goodsId;
    private String shareBillNo;
}
