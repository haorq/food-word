package com.meiyuan.catering.core.constant;

/**
 * 功能描述: 订单模块mq常量定义
 *
 * @author: XiJie Xie
 * @date: 2020/3/24 16:44
 */
public class OrderMqConstant {

    /**
     * 订单延迟交换机定义
     */
    public static final String ORDER_DELAYED_EXCHANGE = "order.direct.delay.exchange";

    /**
     * 订单超时未支付取消订单,延迟消息路由键
     */
    public static final String ORDER_TIME_OUT_EXPIRE_KEY = "ORDER_TIME_OUT_EXPIRE_KEY";


    /**
     * 订单超时未支付取消订单队列
     */
    public static final String ORDER_TIME_OUT_EXPIRE_QUEUE = "ORDER_TIME_OUT_EXPIRE_QUEUE";

    /**
     * 订单完成超时不能申请售后,延迟消息路由键
     */
    public static final String ORDER_AFTER_SALES_EXPIRE_KEY = "ORDER_AFTER_SALES_EXPIRE_KEY";


    /**
     * 订单完成超时不能申请售后队列
     */
    public static final String ORDER_AFTER_SALES_EXPIRE_QUEUE = "ORDER_AFTER_SALES_EXPIRE_QUEUE";

    /**
     * 订单完成超时自动五星好评,延迟消息路由键
     */
    public static final String ORDER_APPRAISE_EXPIRE_KEY = "ORDER_APPRAISE_EXPIRE_KEY";


    /**
     * 订单完成超时自动五星好评队列
     */
    public static final String ORDER_APPRAISE_EXPIRE_QUEUE = "ORDER_APPRAISE_EXPIRE_QUEUE";

    /**
     * 待支付取消订单交换机定义
     */
    public static final String UNPAID_CANCEL_EXCHANGE = "unpaid.cancel.exchange";

    /**
     * 待支付取消订单,路由键
     */
    public static final String UNPAID_CANCEL_KEY = "UNPAID_CANCEL_KEY";


    /**
     * 待支付取消订单队列
     */
    public static final String UNPAID_CANCEL_QUEUE = "UNPAID_CANCEL_QUEUE";

    /**
     * 订单支付成功队列
     */
    public static final String ORDER_PAY_SUCCESS_QUEUE = "ORDER_PAY_SUCCESS_QUEUE";

    /**
     * 订单支付成功交换机定义
     */
    public static final String ORDER_PAY_EXCHANGE = "order.pay.exchange";
    public static final String ORDER_PAY_EXCHANGE_BEAN_NAME = "orderPayExchange";

    /**
     * 订单支付成功,延迟消息路由键
     */
    public static final String ORDER_PAY_KEY = "ORDER_PAY_KEY";

    /**
     * 取消拼团订单队列
     */
    public static final String ORDER_GROUP_CANCEL_QUEUE = "ORDER_GROUP_CANCEL_QUEUE";

    /**
     * 取消拼团订单交换机定义
     */
    public static final String ORDER_GROUP_CANCEL_EXCHANGE = "order.group.cancel.exchange";

    /**
     * 取消拼团订单路由键
     */
    public static final String ORDER_GROUP_CANCEL_KEY = "ORDER_GROUP_CANCEL_KEY";


    /**
     * 下单成功距自提1小时短信通知 交换机定义
     */
    public static final String ORDER_PICK_SMS_EXCHANGE = "order.pick.sms.exchange";

    /**
     * 下单成功距自提1小时短信通知,延迟消息路由键
     */
    public static final String ORDER_PICK_SMS_KEY = "ORDER_PICK_SMS_KEY";


    /**
     * 下单成功距自提1小时短信通知 队列
     */
    public static final String ORDER_PICK_SMS_QUEUE = "ORDER_PICK_SMS_QUEUE";

    /**
     * 订单核销
     */
    public static final String ORDER_CHECK_EXCHANGE="order.check.exchange";
    public static final String ORDER_CHECK_QUEUE="order.check.queue";
    public static final String ORDER_CHECK_KEY="order.check.key";
}
