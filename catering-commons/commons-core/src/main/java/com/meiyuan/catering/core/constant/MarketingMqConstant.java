package com.meiyuan.catering.core.constant;

/**
 * @ClassName MarketingMqConstant
 * @Description 营销模块mq常量定义
 * @Author gz
 * @Date 2020/3/23 15:22
 * @Version 1.1
 */
public class MarketingMqConstant {

    /**
     * 营销延迟交换机定义
     */
    public static final String MARKETING_DELAYED_EXCHANGE = "marketing.direct.delay.exchange";
    public static final String MARKETING_DELAYED_EXCHANGE_BEAN_NAME = "marketingDirectDelayExchange";

    /**
     * 优惠券延迟消息路由键
     */
    public static final String MARKETING_TICKET_EXPIRE_KEY = "MARKETING_TICKET_EXPIRE_KEY";
    public static final String MARKETING_TICKET_PLATFORM_EXPIRE_KEY = "MARKETING_TICKET_PLATFORM_EXPIRE_KEY";


    /**
     * 优惠券发放队列
     */
    public static final String TICKET_PUSH_EXPIRE_QUEUE = "TICKET_PUSH_EXPIRE_QUEUE";
    public static final String TICKET_PUSH_EXPIRE_QUEUE_BEAN_NAME = "ticketPushExpireQueue";
    public static final String TICKET_PLATFORM_PUSH_EXPIRE_QUEUE = "TICKET_PLATFORM_PUSH_EXPIRE_QUEUE";
    public static final String TICKET_PLATFORM_PUSH_EXPIRE_QUEUE_BEAN_NAME = "ticketPlatFormPushExpireQueue";

    /**
     * 用户注册成功下发优惠券队列
     */
    public static final String TICKET_USER_REGISTER_SUCCESS = "TICKET_USER_REGISTER_SUCCESS";
    public static final String TICKET_USER_REGISTER_SUCCESS_BEAN_NAME = "ticketUserRegisterSuccess";


    /**
     * 营销交换机
     */
    public static final String MARKETING_EXCHANGE_BEAN_NAME = "marketingDomainExchangeBean";
    public static final String MARKETING_EXCHANGE = "marketingDomainExchange";
    /**
     * 批量新增队列
     */
    public static final String MARKETING_ADD_BATCH_QUEUE_BEAN_NAME = "MARKETING_ADD_BATCH_QUEUE_BEAN";
    public static final String MARKETING_ADD_BATCH_QUEUE = "MARKETING_ADD_BATCH_QUEUE";
    /**
     * 更新状态队列
     */
    public static final String MARKETING_UPDATE_STATUS_QUEUE_BEAN_NAME = "MARKETING_UPDATE_STATUS_QUEUE_BEAN";
    public static final String MARKETING_UPDATE_STATUS_QUEUE = "MARKETING_UPDATE_STATUS_QUEUE";
    /**
     * 营销特价商品活动冻结队列
     */
    public static final String MARKETING_SPECIAL_STATUS_UPDATE_QUEUE_BEAN_NAME = "MARKETING_SPECIAL_STATUS_UPDATE_QUEUE_BEAN";
    public static final String MARKETING_SPECIAL_STATUS_UPDATE_QUEUE = "MARKETING_SPECIAL_STATUS_UPDATE_QUEUE";

    /**
     * 秒杀队列
     */
//    public static final String MARKETING_SECKILL_QUEUE = "MARKETING_SECKILL_QUEUE";
//    public static final String MARKETING_SECKILL_QUEUE_BEAN_NAME = "marketingSeckillQueue";

    /**
     * 恢复秒杀库存队列
     */
    public static final String MARKETING_SECKILL_STOCK_QUEUE = "MARKETING_SECKILL_STOCK_QUEUE";

    /**
     * 订单支付成功队列
     */
    public static final String MARKETING_SECKILL_ORDER_PAY_SUCCESS_QUEUE = "MARKETING_SECKILL_ORDER_PAY_SUCCESS_QUEUE";
    public static final String MARKETING_SECKILL_ORDER_PAY_SUCCESS_QUEUE_BEAN_NAME = "marketingSeckillOrderPaySuccessQueue";

    /**
     * 秒杀延迟消息路由键
     */
    public static final String MARKETING_SECKILL_EXPIRE_KEY = "MARKETING_SECKILL_EXPIRE_KEY";
    /**
     * 秒杀延迟消息队列
     */
    public static final String MARKETING_SECKILL_EXPIRE_QUEUE = "MARKETING_SECKILL_EXPIRE_QUEUE";
    public static final String MARKETING_SECKILL_EXPIRE_QUEUE_BEAN_NAME = "marketingSeckillExpireQueue";

    /**
     * 团购延迟消息路由键
     */
    public static final String MARKETING_GROUPON_EXPIRE_KEY = "MARKETING_GROUPON_EXPIRE_KEY";
    /**
     * 团购延迟消息队列
     */
    public static final String MARKETING_GROUPON_EXPIRE_QUEUE = "MARKETING_GROUPON_EXPIRE_QUEUE";
    public static final String MARKETING_GROUPON_EXPIRE_QUEUE_BEAN_NAME = "marketingGrouponExpireQueue";

    /**
     * 营销特价商品延迟消息路由键
     */
    public static final String MARKETING_SPECIAL_EXPIRE_KEY = "MARKETING_SPECIAL_EXPIRE_KEY";
    /**
     * 营销特价商品延迟消息队列
     */
    public static final String MARKETING_SPECIAL_EXPIRE_QUEUE = "MARKETING_SPECIAL_EXPIRE_QUEUE";
    public static final String MARKETING_SPECIAL_EXPIRE_QUEUE_BEAN_NAME = "marketingSpecialExpireQueue";

    /**
     * 商品信息更新队列
     */
    public static final String MARKETING_GOODS_UPDATE_QUEUE = "MARKETING_GOODS_UPDATE_QUEUE";
    public static final String MARKETING_GOODS_UPDATE_QUEUE_BEAN_NAME = "marketingGoodsUpdateQueue";

    /**
     * 团购失败退款队列
     */
    public static final String GROUP_FAIL_QUEUE = "GROUP_FAIL_QUEUE";
    public static final String GROUP_FAIL_QUEUE_BEAN_NAME = "groupFailQueue";

    /**
     * 团购失败交换机定义
     */
    public static final String GROUP_FAIL_EXCHANGE = "group.fail.exchange";
    public static final String GROUP_FAIL_EXCHANGE_BEAN_NAME = "groupFailExchange";

    /**
     * 团购失败路由键
     */
    public static final String GROUP_FAIL_KEY = "GROUP_FAIL_KEY";

    /**
     * 拼团成功队列
     */
    public static final String GROUP_SUCCESS_QUEUE = "GROUP_SUCCESS_QUEUE";
    public static final String GROUP_SUCCESS_QUEUE_BEAN_NAME = "groupSuccessQueue";

    /**
     * 拼团成功交换机定义
     */
    public static final String GROUP_SUCCESS_EXCHANGE = "group.success.exchange";
    public static final String GROUP_SUCCESS_EXCHANGE_BEAN_NAME = "groupSuccessExchange";

    /**
     * 拼团成功路由键
     */
    public static final String GROUP_SUCCESS_KEY = "GROUP_SUCCESS_KEY";
}
