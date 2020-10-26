package com.meiyuan.catering.order.config;

import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.constant.RabbitMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RabbitMqConfig
 * @Description
 * @Author xxj
 * @Date 2020/3/23 15:28
 * @Version 1.1
 */
@Configuration
public class OrderRabbitMqConfig {
    /**
     * 订单直传延迟队列交换机
     *
     * @return the exchange
     */
    @Bean
    public Exchange directDelayExchange() {
        return ExchangeBuilder.directExchange(OrderMqConstant.ORDER_DELAYED_EXCHANGE)
                .withArgument(RabbitMqConstant.PluginExchangeArguments.X_DELAYED_TYPE, ExchangeTypes.DIRECT)
                .durable(Boolean.TRUE)
                .delayed()
                .build();
    }

    /**
     * 订单超时未支付取消订单队列
     *
     * @return
     */
    @Bean
    public Queue orderTimeOutPushExpireQueue() {
        return QueueBuilder.durable(OrderMqConstant.ORDER_TIME_OUT_EXPIRE_QUEUE).build();
    }

    /**
     * 订单超时未支付取消订单,延迟消息路由键绑定
     *
     * @return
     */
    @Bean
    public Binding orderTimeOutPushExpireBinding() {
        return BindingBuilder.bind(orderTimeOutPushExpireQueue())
                .to(directDelayExchange()).with(OrderMqConstant.ORDER_TIME_OUT_EXPIRE_KEY).noargs();
    }

    /**
     * 订单超时未支付取消订单队列
     *
     * @return
     */
    @Bean
    public Queue orderAfterSalesPushExpireQueue() {
        return QueueBuilder.durable(OrderMqConstant.ORDER_AFTER_SALES_EXPIRE_QUEUE).build();
    }

    /**
     * 订单超时未支付取消订单,延迟消息路由键绑定
     *
     * @return
     */
    @Bean
    public Binding orderAfterSalesPushExpireBinding() {
        return BindingBuilder.bind(orderAfterSalesPushExpireQueue())
                .to(directDelayExchange()).with(OrderMqConstant.ORDER_AFTER_SALES_EXPIRE_KEY).noargs();
    }

    /**
     * 订单超时未支付取消订单队列
     *
     * @return
     */
    @Bean
    public Queue orderAppraisePushExpireQueue() {
        return QueueBuilder.durable(OrderMqConstant.ORDER_APPRAISE_EXPIRE_QUEUE).build();
    }

    /**
     * 订单超时未支付取消订单,延迟消息路由键绑定
     *
     * @return
     */
    @Bean
    public Binding orderAppraisePushExpireBinding() {
        return BindingBuilder.bind(orderAppraisePushExpireQueue())
                .to(directDelayExchange()).with(OrderMqConstant.ORDER_APPRAISE_EXPIRE_KEY).noargs();
    }

    /**
     * 功能描述: 待支付取消订单广播交换机
     *
     * @param
     * @return: org.springframework.amqp.core.Exchange
     */
    @Bean
    public Exchange unpaidCancelFanoutExchange() {
        return new FanoutExchange(OrderMqConstant.UNPAID_CANCEL_EXCHANGE);
    }

    /**
     * 待支付取消订单队列
     *
     * @return
     */
    @Bean
    public Queue unpaidCancelQueue() {
        return QueueBuilder.durable(OrderMqConstant.UNPAID_CANCEL_QUEUE).build();
    }

    /**
     * 待支付取消订单,路由键绑定
     *
     * @return
     */
    @Bean
    public Binding unpaidCancelBinding() {
        return BindingBuilder.bind(unpaidCancelQueue())
                .to(directDelayExchange()).with(OrderMqConstant.UNPAID_CANCEL_KEY).noargs();
    }


    /**
     * 订单核销
     *
     * @return
     */
    @Bean
    public Exchange orderCheckTopicExchange() {
        return ExchangeBuilder
                .topicExchange(OrderMqConstant.ORDER_CHECK_EXCHANGE)
                .durable(Boolean.TRUE)
                .build();
    }

    /**
     * 订单核销
     *
     * @return
     */
    @Bean
    public Queue orderCheckQueue() {
        return QueueBuilder.durable(OrderMqConstant.ORDER_CHECK_QUEUE).build();
    }

    /**
     * 订单核销
     *
     * @return
     */
    @Bean
    public Binding orderCheckBinding() {
        return BindingBuilder
                .bind(orderCheckQueue())
                .to(orderCheckTopicExchange())
                .with(OrderMqConstant.ORDER_CHECK_KEY)
                .noargs();
    }


    @Bean
    public Exchange fanoutExchange() {
        return new FanoutExchange(OrderMqConstant.ORDER_PAY_EXCHANGE);
    }

    /**
     * 订单支付成功队列
     *
     * @return
     */
    @Bean
    public Queue orderPaySuccessQueue() {
        return QueueBuilder.durable(OrderMqConstant.ORDER_PAY_SUCCESS_QUEUE).build();
    }

    /**
     * 订单支付成功队列,路由键绑定
     *
     * @return
     */
    @Bean
    public Binding orderPayExchangeBinding() {
        return BindingBuilder.bind(orderPaySuccessQueue())
                .to(fanoutExchange()).with(OrderMqConstant.ORDER_PAY_KEY).noargs();
    }

    @Bean
    public Exchange orderGroupCancelExchange() {
        return new FanoutExchange(OrderMqConstant.ORDER_GROUP_CANCEL_EXCHANGE);
    }

    /**
     * 取消拼团订单队列
     *
     * @return
     */
    @Bean
    public Queue orderGroupCancelQueue() {
        return QueueBuilder.durable(OrderMqConstant.ORDER_GROUP_CANCEL_QUEUE).build();
    }

    /**
     * 取消拼团订单,路由键绑定
     *
     * @return
     */
    @Bean
    public Binding orderGroupCancelExchangeBinding() {
        return BindingBuilder.bind(orderGroupCancelQueue())
                .to(orderGroupCancelExchange()).with(OrderMqConstant.ORDER_GROUP_CANCEL_KEY).noargs();
    }


    /**
     * 下单成功距自提1小时短信通知
     *
     * @return the exchange
     */
    @Bean
    public Exchange pickSmsExchange() {
        return ExchangeBuilder.directExchange(OrderMqConstant.ORDER_PICK_SMS_EXCHANGE)
                .withArgument(RabbitMqConstant.PluginExchangeArguments.X_DELAYED_TYPE, ExchangeTypes.DIRECT)
                .durable(Boolean.TRUE)
                .delayed()
                .build();
    }

    /**
     * 下单成功距自提1小时短信通知
     *
     * @return
     */
    @Bean
    public Queue pickSmsQueue() {
        return QueueBuilder.durable(OrderMqConstant.ORDER_PICK_SMS_QUEUE).build();
    }

    /**
     * 下单成功距自提1小时短信通知,延迟消息路由键绑定
     *
     * @return
     */
    @Bean
    public Binding pickSmsBinding() {
        return BindingBuilder.bind(pickSmsQueue())
                .to(pickSmsExchange()).with(OrderMqConstant.ORDER_PICK_SMS_KEY).noargs();
    }

}
