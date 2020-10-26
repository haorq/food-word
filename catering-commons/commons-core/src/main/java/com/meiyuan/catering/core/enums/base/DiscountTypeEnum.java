package com.meiyuan.catering.core.enums.base;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author MeiTao
 * @Date 2020/8/12 0012 16:01
 * @Description 简单描述 : 优惠活动类型 : 1：折扣商品，2：进店领券，3：满减优惠券，4，秒杀活动，5：团购活动
 * @Since version-1.3.0
 */
@Getter
@Slf4j
public enum DiscountTypeEnum {
    /**
     * 1：折扣商品
     */
    GOODS_DISCOUNT(Values.GOODS_DISCOUNT,"1：折扣商品"),

    TICKET_GET_DISCOUNT(Values.TICKET_GET_DISCOUNT,"2：进店领券/店内领券：对应商户PC端的『店内领券』活动"),

    TICKET_SEND_DISCOUNT(Values.TICKET_SEND_DISCOUNT,"3：满减优惠券/店外发券 ：对应平台自动发放的券、商户PC端的『店外发券』活动"),

    SECKILL_DISCOUNT(Values.SECKILL_DISCOUNT,"4:秒杀活动"),

    GROUPON_DISCOUNT(Values.GROUPON_DISCOUNT,"5:团购活动"),

    DELIVERY_DISCOUNT(Values.DELIVERY_DISCOUNT,"6:配送优惠");
    private Integer status;
    private String desc;
    DiscountTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static DiscountTypeEnum parse(int status) {
        for (DiscountTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        log.error("商家列表查询查询类型传入错误：" + status );
        return GOODS_DISCOUNT;
    }

    public static class  Values{
        private static final Integer GOODS_DISCOUNT = 1;
        private static final Integer TICKET_GET_DISCOUNT = 2;
        private static final Integer TICKET_SEND_DISCOUNT = 3;
        private static final Integer SECKILL_DISCOUNT = 4;
        private static final Integer GROUPON_DISCOUNT = 5;
        private static final Integer DELIVERY_DISCOUNT = 6;
    }
}
