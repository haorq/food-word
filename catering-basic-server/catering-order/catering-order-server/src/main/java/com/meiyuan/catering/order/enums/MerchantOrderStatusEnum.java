package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 商户订单详情的订单状态枚举 1：待配送；2：待自取；3：已完成；4：已取消；5：已失效；6：待退款；7：已退款；
 *
 * @author zengzhangni
 * @date 2019/3/20
 */
@Getter
public enum MerchantOrderStatusEnum {
    /**
     * 待配送
     */
    WAIT_DELIVERY(1, "待配送"),
    /**
     * 待取餐
     */
    WAIT_TAKEN(2, "待取餐"),
    /**
     * 已完成
     */
    DONE(3, "已完成"),
    /**
     * 已取消
     */
    CANCELED(4, "已取消"),
    /**
     * 已失效
     */
    OFF(5, "已失效"),
    /**
     * 待退款
     */
    UN_REFUND(6, "待退款"),
    /**
     * 已退款
     */
    REFUND(7, "已退款"),
    /**
     * 团购中
     */
    GROUP(8, "团购中");

    /**
     * 枚举code
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String desc;

    MerchantOrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
