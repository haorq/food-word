package com.meiyuan.catering.marketing.config;

import com.meiyuan.catering.core.constant.*;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RabbitMqConfig
 * @Description
 * @Author gz
 * @Date 2020/3/23 15:28
 * @Version 1.1
 */
@Configuration
public class RabbitMqConfig {
    /**
     * 延迟队列交换机
     *
     * @return the exchange
     */
    @Bean(MarketingMqConstant.MARKETING_DELAYED_EXCHANGE_BEAN_NAME)
    public Exchange directDelayExchange() {
        return ExchangeBuilder.directExchange(MarketingMqConstant.MARKETING_DELAYED_EXCHANGE)
                .withArgument(RabbitMqConstant.PluginExchangeArguments.X_DELAYED_TYPE, ExchangeTypes.DIRECT)
                .durable(Boolean.TRUE)
                .delayed()
                .build();
    }

    /**
     * 优惠券发放消息队列
     *
     * @return
     */
    @Bean(MarketingMqConstant.TICKET_PUSH_EXPIRE_QUEUE_BEAN_NAME)
    public Queue ticketPushExpireQueue() {
        return QueueBuilder.durable(MarketingMqConstant.TICKET_PUSH_EXPIRE_QUEUE).build();
    }

    /**
     * 优惠券发放消息队列
     *
     * @return
     */
    @Bean(MarketingMqConstant.TICKET_PLATFORM_PUSH_EXPIRE_QUEUE_BEAN_NAME)
    public Queue ticketPlatFormPushExpireQueue() {
        return QueueBuilder.durable(MarketingMqConstant.TICKET_PLATFORM_PUSH_EXPIRE_QUEUE).build();
    }

    /**
     * 优惠券发放路由绑定
     *
     * @return
     */
    @Bean
    public Binding ticketPushExpireBinding() {
        return BindingBuilder.bind(ticketPushExpireQueue())
                .to(directDelayExchange()).with(MarketingMqConstant.MARKETING_TICKET_EXPIRE_KEY).noargs();
    }
    /**
     * 优惠券发放路由绑定
     *
     * @return
     */
    @Bean
    public Binding ticketPlatFormPushExpireBinding() {
        return BindingBuilder.bind(ticketPlatFormPushExpireQueue())
                .to(directDelayExchange()).with(MarketingMqConstant.MARKETING_TICKET_PLATFORM_EXPIRE_KEY).noargs();
    }

    /**
     * 营销活动交换机
     *
     * @return the exchange
     */
    @Bean(MarketingMqConstant.MARKETING_EXCHANGE_BEAN_NAME)
    public Exchange marketingDomainExchange() {
        Map<String, Object> args = new HashMap<>(16);
        Exchange customExchange = new DirectExchange(
                MarketingMqConstant.MARKETING_EXCHANGE, true, false, args
        );
        return customExchange;
    }

    /**
     * 营销活动批量队列
     *
     * @return
     */
    @Bean(MarketingMqConstant.MARKETING_ADD_BATCH_QUEUE_BEAN_NAME)
    public Queue marketingAddBatchQueue() {
        Queue queue = new Queue(MarketingMqConstant.MARKETING_ADD_BATCH_QUEUE, true);
        return queue;
    }

    @Bean
    public Binding marketingAddBatchQueueBinding() {
        return BindingBuilder.bind(marketingAddBatchQueue()).to(marketingDomainExchange()).with(MarketingMqConstant.MARKETING_ADD_BATCH_QUEUE).noargs();
    }

    /**
     * 营销活动状态更新队列
     *
     * @return
     */
    @Bean(MarketingMqConstant.MARKETING_UPDATE_STATUS_QUEUE_BEAN_NAME)
    public Queue marketingStatusUpdateQueue() {
        Queue queue = new Queue(MarketingMqConstant.MARKETING_UPDATE_STATUS_QUEUE, true);
        return queue;
    }

    @Bean
    public Binding marketingStatusUpdateQueueBinding() {
        return BindingBuilder.bind(marketingStatusUpdateQueue()).to(marketingDomainExchange()).with(MarketingMqConstant.MARKETING_UPDATE_STATUS_QUEUE).noargs();
    }

    /**
     * 营销特价商品活动冻结队列
     * @return
     */
    @Bean(MarketingMqConstant.MARKETING_SPECIAL_STATUS_UPDATE_QUEUE_BEAN_NAME)
    public Queue marketingSpecialStatusUpdateQueue() {
        return new Queue(MarketingMqConstant.MARKETING_SPECIAL_STATUS_UPDATE_QUEUE, true);
    }

    @Bean
    public Binding marketingSpecialStatusUpdateQueueBinding() {
        return BindingBuilder.bind(marketingSpecialStatusUpdateQueue()).to(marketingDomainExchange()).with(MarketingMqConstant.MARKETING_SPECIAL_STATUS_UPDATE_QUEUE).noargs();
    }

    /**
     * 用户注册成功广播交换机
     */
    @Bean(UserMqConstant.USER_ADD_FANOUT_EXCHANGE_BEAN_NAME)
    public FanoutExchange userAddFanoutExchange() {
        return new FanoutExchange(UserMqConstant.USER_ADD_FANOUT_EXCHANGE);
    }

    @Bean(MarketingMqConstant.TICKET_USER_REGISTER_SUCCESS_BEAN_NAME)
    public Queue userRegisterSuccessQueue() {
        return new Queue(MarketingMqConstant.TICKET_USER_REGISTER_SUCCESS, true);
    }

    @Bean
    public Binding useRegisterSuccessQueueBinding() {
        return BindingBuilder.bind(userRegisterSuccessQueue()).to(userAddFanoutExchange());
    }


    /**
     * 秒杀定时下架消息队列
     *
     * @return
     */
    @Bean(MarketingMqConstant.MARKETING_SECKILL_EXPIRE_QUEUE_BEAN_NAME)
    public Queue seckillExpireQueue() {
        return QueueBuilder.durable(MarketingMqConstant.MARKETING_SECKILL_EXPIRE_QUEUE).build();
    }

    /**
     * 秒杀定时下架路由绑定
     *
     * @return
     */
    @Bean
    public Binding seckillExpireBinding() {
        return BindingBuilder.bind(seckillExpireQueue())
                .to(directDelayExchange()).with(MarketingMqConstant.MARKETING_SECKILL_EXPIRE_KEY).noargs();
    }

    /**
     * 团购定时下架消息队列
     *
     * @return
     */
    @Bean(MarketingMqConstant.MARKETING_GROUPON_EXPIRE_QUEUE_BEAN_NAME)
    public Queue grouponExpireQueue() {
        return QueueBuilder.durable(MarketingMqConstant.MARKETING_GROUPON_EXPIRE_QUEUE).build();
    }

    /**
     * 团购定时下架路由绑定
     *
     * @return
     */
    @Bean
    public Binding grouponExpireBinding() {
        return BindingBuilder.bind(grouponExpireQueue())
                .to(directDelayExchange()).with(MarketingMqConstant.MARKETING_GROUPON_EXPIRE_KEY).noargs();
    }

    /**
     * 营销特价商品延迟队列
     * @return
     */
    @Bean(MarketingMqConstant.MARKETING_SPECIAL_EXPIRE_QUEUE_BEAN_NAME)
    public Queue specialExpireQueue() {
        return QueueBuilder.durable(MarketingMqConstant.MARKETING_SPECIAL_EXPIRE_QUEUE).build();
    }

    /**
     * 营销特价商品活动延迟消息路由键绑定
     * @return
     */
    @Bean
    public Binding specialExpireBinding() {
        return BindingBuilder.bind(specialExpireQueue())
                .to(directDelayExchange()).with(MarketingMqConstant.MARKETING_SPECIAL_EXPIRE_KEY).noargs();
    }

    /**
     * 订单支付成功广播消息
     *
     * @return
     */
    @Bean(OrderMqConstant.ORDER_PAY_EXCHANGE_BEAN_NAME)
    public FanoutExchange orderSuccessFanoutExchange() {
        return new FanoutExchange(OrderMqConstant.ORDER_PAY_EXCHANGE);
    }

    @Bean(MarketingMqConstant.MARKETING_SECKILL_ORDER_PAY_SUCCESS_QUEUE_BEAN_NAME)
    public Queue marketingOrderPaySuccessQueue() {
        return new Queue(MarketingMqConstant.MARKETING_SECKILL_ORDER_PAY_SUCCESS_QUEUE, true);
    }

    @Bean
    public Binding orderPaySuccessQueueBinding() {
        return BindingBuilder.bind(marketingOrderPaySuccessQueue()).to(orderSuccessFanoutExchange());
    }

    /**
     * 商品信息修改广播消息
     *
     * @return
     */
    @Bean(GoodsMqConstant.GOODS_ADD_UPDATE_FANOUT_EXCHANGE_BEAN_NAME)
    public FanoutExchange goodsUpdateFanoutExchange() {
        return new FanoutExchange(GoodsMqConstant.GOODS_ADD_UPDATE_FANOUT_EXCHANGE);
    }

    @Bean(MarketingMqConstant.MARKETING_GOODS_UPDATE_QUEUE_BEAN_NAME)
    public Queue goodsUpdateQueue() {
        return new Queue(MarketingMqConstant.MARKETING_GOODS_UPDATE_QUEUE, true);
    }

    @Bean
    public Binding goodsUpdateQueueBinding() {
        return BindingBuilder.bind(goodsUpdateQueue()).to(goodsUpdateFanoutExchange());
    }

    @Bean(MarketingMqConstant.GROUP_FAIL_EXCHANGE_BEAN_NAME)
    public Exchange groupFailExchange() {
        return new DirectExchange(MarketingMqConstant.GROUP_FAIL_EXCHANGE);
    }

    /**
     * 订单支付成功队列
     *
     * @return
     */
    @Bean(MarketingMqConstant.GROUP_FAIL_QUEUE_BEAN_NAME)
    public Queue groupFailQueue() {
        return QueueBuilder.durable(MarketingMqConstant.GROUP_FAIL_QUEUE).build();
    }

    /**
     * 订单支付成功队列,路由键绑定
     *
     * @return
     */
    @Bean
    public Binding groupFailExchangeBinding() {
        return BindingBuilder.bind(groupFailQueue())
                .to(groupFailExchange()).with(MarketingMqConstant.GROUP_FAIL_KEY).noargs();
    }

    @Bean(MarketingMqConstant.GROUP_SUCCESS_EXCHANGE_BEAN_NAME)
    public Exchange groupSuccessExchange() {
        return new DirectExchange(MarketingMqConstant.GROUP_SUCCESS_EXCHANGE);
    }

    /**
     * 拼团成功队列
     *
     * @return
     */
    @Bean(MarketingMqConstant.GROUP_SUCCESS_QUEUE_BEAN_NAME)
    public Queue groupSuccessQueue() {
        return QueueBuilder.durable(MarketingMqConstant.GROUP_SUCCESS_QUEUE).build();
    }

    /**
     * 拼团成功,路由键绑定
     *
     * @return
     */
    @Bean
    public Binding orderGroupSuccessExchangeBinding() {
        return BindingBuilder.bind(groupSuccessQueue())
                .to(groupSuccessExchange()).with(MarketingMqConstant.GROUP_SUCCESS_KEY).noargs();
    }



    /**
     * 方法描述   商品取消授权
     * @author: lhm
     * @date: 2020/8/11 18:28
     * @param
     * @return: {@link }
     * @version 1.3.0
     **/
    @Bean(GoodsMqConstant.GOODS_DELETE_FANOUT_EXCHANGE_BEAN_NAME)
    public FanoutExchange goodsDeleteFanoutExchange() {
        return new FanoutExchange(GoodsMqConstant.GOODS_DELETE_FANOUT_EXCHANGE);
    }


}
