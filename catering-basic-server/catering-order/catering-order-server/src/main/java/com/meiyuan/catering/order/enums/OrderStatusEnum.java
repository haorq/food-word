package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author zengzhangni
 * @date 2019/3/20
 */
@Getter
public enum OrderStatusEnum {
    /**
     * 待付款
     */
    UNPAID(Values.UNPAID, ValuesDesc.UNPAID),
    /**
     * 待接单
     */
    WAIT_ORDERS(Values.WAIT_ORDERS, ValuesDesc.WAIT_ORDERS),
    /**
     * 待配送
     */
    WAIT_DELIVERY(Values.WAIT_DELIVERY, ValuesDesc.WAIT_DELIVERY),
    /**
     * 待取餐
     */
    WAIT_TAKEN(Values.WAIT_TAKEN, ValuesDesc.WAIT_TAKEN),
    /**
     * 已完成
     */
    DONE(Values.DONE, ValuesDesc.DONE),
    /**
     * 已取消
     */
    CANCELED(Values.CANCELED, ValuesDesc.CANCELED),
    /**
     * 已关闭
     */
    OFF(Values.OFF, ValuesDesc.OFF),
    /**
     * 团购中
     */
    GROUP(Values.GROUP, ValuesDesc.GROUP);

    private final Integer value;
    private String desc;

    OrderStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    OrderStatusEnum(Integer value) {
        this.value = value;
    }

    public static class Values {
        public static final int UNPAID = 1;
        public static final int WAIT_ORDERS = 2;
        public static final int WAIT_DELIVERY = 3;
        public static final int WAIT_TAKEN = 4;
        public static final int DONE = 5;
        public static final int CANCELED = 6;
        public static final int OFF = 7;
        public static final int GROUP = 8;
    }

    public static class ValuesDesc {
        public static final String UNPAID = "待付款";
        public static final String WAIT_ORDERS = "待接单";
        public static final String WAIT_DELIVERY = "待配送";
        public static final String WAIT_TAKEN = "待取餐";
        public static final String DONE = "已完成";
        public static final String CANCELED = "已取消";
        public static final String OFF = "已关闭";
        public static final String GROUP = "团购中";
    }

    public static Integer getValue(OrderStatusEnum orderStateEnum) {
        return orderStateEnum.value;
    }

    public static OrderStatusEnum getByValue(Integer status) {
        for (OrderStatusEnum orderStateEnum : values()) {
            if (orderStateEnum.value.equals(status)) {
                return orderStateEnum;
            }
        }
        return null;
    }

    public static String parse(Integer status) {
        for (OrderStatusEnum item : values()) {
            if (item.getValue().compareTo(status) == 0) {
                return item.getDesc();
            }
        }
        return "未知订单状态";
    }

    public int value() {
        return this.value;
    }
}
