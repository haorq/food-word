package com.meiyuan.catering.merchant.config;

import com.meiyuan.catering.core.constant.MerchantMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author MeiTao
 * @Date  2020/4/7 0007 16:31
 */
@Configuration
public class MerchantMqConfig {


    /*========================商户基本信息修改===============================*/

    @Bean(name = MerchantMqConstant.MERCHANT_INFO_QUEUE_BEAN)
    public Queue merchantInfoQueue() {
        return QueueBuilder.durable(MerchantMqConstant.MERCHANT_INFO_QUEUE).build();
    }

    @Bean(name = MerchantMqConstant.MERCHANT_INFO_FANOUT_EXCHANGE_BEAN)
    public FanoutExchange merchantInfoFanoutExchange() {
        return new FanoutExchange(MerchantMqConstant.MERCHANT_INFO_FANOUT_EXCHANGE);
    }

    @Bean(name = MerchantMqConstant.MERCHANT_INFO_BINDING_BEAN)
    public Binding merchantInfoBinding() {
        return BindingBuilder.bind(merchantInfoQueue())
                .to(merchantInfoFanoutExchange());
    }
}
