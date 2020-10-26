package com.meiyuan.catering.order.enums;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单配送状态枚举
 *
 * @author lh
 */
public enum OrderDeliveryStatusEnum {
    wait_accept(1, "待接单"),
    wait_take(2, "待取货"),
    delivery(3, "配送中"),
    finish(4, "已完成"),
    cancel(5, "已取消"),
    expiration(7, "已过期"),
    send_other(8, "指派单"),
    error_back_in(9, "妥投异常之物品返回中"),
    error_back_finish(10, "妥投异常之物品返回完成"),
    driver_arrived(100, "骑士到店"),
    fail(1000, "创建达达运单失败");


    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    OrderDeliveryStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String parseCode(Integer code) {
        for (OrderDeliveryStatusEnum orderDeliveryStatusEnum : OrderDeliveryStatusEnum.values()) {
            if (orderDeliveryStatusEnum.code.equals(code)) {
                return orderDeliveryStatusEnum.msg;
            }
        }
        return "";
    }

    /**
     * 获取指定状态枚举值  用于核销校验
     * @return
     */
    public static List<Integer> getCheckStatus() {
        List<Integer> list = new ArrayList<>();
        list.add(OrderDeliveryStatusEnum.wait_accept.getCode());
        list.add(OrderDeliveryStatusEnum.wait_take.getCode());
        list.add(OrderDeliveryStatusEnum.delivery.getCode());
        list.add(OrderDeliveryStatusEnum.error_back_in.getCode());
        list.add(OrderDeliveryStatusEnum.driver_arrived.getCode());
        return list;
    }

}
