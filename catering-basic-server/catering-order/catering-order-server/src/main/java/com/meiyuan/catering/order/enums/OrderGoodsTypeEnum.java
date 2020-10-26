package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 订单商品类型1--普通商品，2--秒杀商品，3--团购商品
 *
 * @author xxj
 * @date 2019/3/20
 */
@Getter
public enum OrderGoodsTypeEnum {
    /**
     * 普通商品
     */
    ORDINARY(Values.ORDINARY),
    /**
     * 秒杀商品
     */
    SECONDS(Values.SECONDS),
    /**
     * 团购商品
     */
    BULK(Values.BULK),

    /**
     * 特价商品
     */
    SPECIAL(Values.SPECIAL);

    private final Integer value;

    OrderGoodsTypeEnum(Integer value) {
        this.value = value;
    }

    public static class Values {
        public static final int ORDINARY = 1;
        public static final int SECONDS = 2;
        public static final int BULK = 3;
        public static final int SPECIAL = 4;
    }

    public static Integer getValue(OrderGoodsTypeEnum orderStateEnum) {
        return orderStateEnum.value;
    }

    public static OrderGoodsTypeEnum getByValue(Integer status) {
        for (OrderGoodsTypeEnum orderStateEnum : values()) {
            if (orderStateEnum.value.equals(status)) {
                return orderStateEnum;
            }
        }
        return null;
    }

    public int value() {
        return this.value;
    }
}
