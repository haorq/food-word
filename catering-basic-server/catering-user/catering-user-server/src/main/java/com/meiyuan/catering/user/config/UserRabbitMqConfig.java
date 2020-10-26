package com.meiyuan.catering.user.config;

import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.constant.UserMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lhm
 * @date 2020/3/26 17:15
 **/
@Configuration
public class UserRabbitMqConfig {

    /**
     * 新用户新增队列
     *
     * @return
     */
    @Bean(UserMqConstant.USER_ADD_QUEUE_BEAN_NAME)
    public Queue userAddQueue() {
        Queue queue = new Queue(UserMqConstant.USER_ADD__QUEUE, true);
        return queue;
    }


    @Bean(UserMqConstant.USER_ADD_FANOUT_EXCHANGE_BEAN_NAME)
    public FanoutExchange userAddFanoutExchange() {
        return new FanoutExchange(UserMqConstant.USER_ADD_FANOUT_EXCHANGE);
    }

   @Bean(UserMqConstant.USER_BINDING_BEAN_NAME)
    public Binding userAddQueueQueueBinding() {
        return BindingBuilder.bind(userAddQueue()).to(userAddFanoutExchange());
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
     * 秒杀队列
     * @return
     */
    /*@Bean
    public Queue seckillQueue(){
        return new Queue(MarketingMqConstant.MARKETING_SECKILL_QUEUE, true);
    }
    @Bean
    public Binding seckillQueueBinding(){
        return BindingBuilder.bind(seckillQueue()).to(marketingDomainExchange()).with(MarketingMqConstant.MARKETING_SECKILL_QUEUE).noargs();
    }*/

    /**
     * 秒杀还原库存队列
     * @return
     */
    @Bean
    public Queue seckillStockQueue(){
        return new Queue(MarketingMqConstant.MARKETING_SECKILL_STOCK_QUEUE, true);
    }
    @Bean
    public Binding seckillStockQueueBinding(){
        return BindingBuilder.bind(seckillStockQueue()).to(marketingDomainExchange()).with(MarketingMqConstant.MARKETING_SECKILL_STOCK_QUEUE).noargs();
    }

    /**
     * 商户修改售卖模式信息
     *
     * @return
     */
//    @Bean
//    public Queue merchantUpdateQueue() {
//        Queue queue = new Queue(MerchantMqConstant.MERCHANT_UPDATE_QUEUE, true);
//        return queue;
//    }
//    @Bean(MerchantMqConstant.MERCHANT_UPDATE_FANOUT_EXCHANGE_BEAN_NAME)
//    public Exchange merchantDomainExchange() {
//        Map<String, Object> args = new HashMap<>(16);
//        Exchange customExchange = new DirectExchange(
//                MerchantMqConstant.MERCHANT_UPDATE_FANOUT_EXCHANGE, true, false, args
//        );
//        return customExchange;
//    }
//    @Bean
//    public Binding merchantUpdateQueueQueueBinding() {
//        return BindingBuilder.bind(merchantUpdateQueue()).to(merchantDomainExchange()).with(MerchantMqConstant.MERCHANT_UPDATE_QUEUE).noargs();
//
//    }

    /**
     * @description 商户商品上下架-用户购物车处理队列
     * @author yaozou
     * @date 2020/4/9 14:42
     * @since v1.0.0
     */
//    @Bean
//    public Queue merchantGoodsUpDownUserCartQueue() {
//        Queue queue = new Queue(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_USER_CART_QUEUE, true);
//        return queue;
//    }
//    @Bean(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_FANOUT_EXCHANGE_BEAN_NAME)
//    public FanoutExchange merchantGoodsUpDownFanoutExchange() {
//        return new FanoutExchange(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_FANOUT_EXCHANGE);
//    }
//    @Bean
//    public Binding merchantGoodsUpDownUserCartQueueBinding() {
//        return BindingBuilder.bind(merchantGoodsUpDownUserCartQueue()).to(merchantGoodsUpDownFanoutExchange());
//    }

    /**
     * @description 商品信息改变-购物车对应商品改变
     * @author yaozou
     * @date 2020/4/15 10:37
     * @since v1.0.0
     */
//    @Bean(GoodsMqConstant.GOODS_ADD_UPDATE_CART_QUEUE)
//    public Queue goodsUpdateCartQueue() {
//        return new Queue(GoodsMqConstant.GOODS_ADD_UPDATE_CART_QUEUE, true);
//    }

//    @Bean
//    public Binding goodsUpdateCartQueueBinding() {
//        return BindingBuilder.bind(goodsUpdateCartQueue()).to(new FanoutExchange(GoodsMqConstant.GOODS_ADD_UPDATE_FANOUT_EXCHANGE));
//    }


}
