package com.meiyuan.catering.es.config;

import com.meiyuan.catering.core.constant.GoodsMqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * @author wxf
 */
@Configuration
public class EsRabbitMqConfig {

	public static final Charset RABBITMQ_MESSAGE_CHARSET = StandardCharsets.UTF_8;

	/**
	 * 商品修改广播信息交换机
	 *
	 * @author: wxf
	 * @date: 2020/3/23 14:46
	 * @return: {@link FanoutExchange}
	 **/
	@Bean(GoodsMqConstant.GOODS_ADD_UPDATE_FANOUT_EXCHANGE_BEAN_NAME)
	public FanoutExchange goodsAddUpdateFanoutExchange() {
		return new FanoutExchange(GoodsMqConstant.GOODS_ADD_UPDATE_FANOUT_EXCHANGE);
	}


	/**
	 * 商品新增修改广播队列
	 *
	 * @author: wxf
	 * @date: 2020/3/23 17:55
	 * @return: {@link Queue}
	 **/
	@Bean(GoodsMqConstant.GOODS_ADD_UPDATE_FANOUT_QUEUE_BEAN_NAME)
	public Queue goodsAddUpdateFanoutQueue() {
		return new Queue(GoodsMqConstant.GOODS_ADD_UPDATE_FANOUT_QUEUE, true);
	}

	/**
	 * 商品新增修改广播消息绑定
	 *
	 * @author: wxf
	 * @date: 2020/3/23 17:57
	 * @return: {@link Binding}
	 **/
	@Bean(GoodsMqConstant.GOODS_BINDING_BEAN_NAME)
	public Binding goodsBrandUpdateBinding() {
		return BindingBuilder.bind(goodsAddUpdateFanoutQueue()).to(goodsAddUpdateFanoutExchange());
	}

	/**
	 * 商户商品上下架修改广播信息交换机
	 *
	 * @author: wxf
	 * @date: 2020/3/23 14:46
	 * @return: {@link FanoutExchange}
	 **/
	@Bean(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_FANOUT_EXCHANGE_BEAN_NAME)
	public FanoutExchange merchantGoodsStatusUpdateFanoutExchange() {
		return new FanoutExchange(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_FANOUT_EXCHANGE);
	}


	/**
	 * 商户商品上下架修改广播队列
	 *
	 * @author: wxf
	 * @date: 2020/3/23 17:55
	 * @return: {@link Queue}
	 **/
	@Bean(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_ES_UPDATE_QUEUE_BEAN_NAME)
	public Queue merchantGoodsStatusUpdateFanoutQueue() {
		return new Queue(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_ES_UPDATE_QUEUE, true);
	}

	/**
	 * 商户商品上下架修改-同步ES处理队列路由
	 *
	 * @author: wxf
	 * @date: 2020/3/23 17:57
	 * @return: {@link Binding}
	 **/
	@Bean(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_ES_UPDATE_QUEUE_BINDING_BEAN_NAME)
	public Binding merchantGoodsStatusUpdateBinding() {
		return BindingBuilder.bind(merchantGoodsStatusUpdateFanoutQueue()).to(merchantGoodsStatusUpdateFanoutExchange());
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

	/**
	 * 方法描述   商品取消授权
	 * @author: lhm
	 * @date: 2020/8/11 18:28
	 * @param
	 * @return: {@link }
	 * @version 1.3.0
	 **/
	@Bean(GoodsMqConstant.GOODS_DELETE_FANOUT_QUEUE_BEAN_NAME)
	public Queue goodsDeleteFanoutQueue() {
		return new Queue(GoodsMqConstant.GOODS_DELETE_FANOUT_QUEUE,true);
	}

	@Bean(GoodsMqConstant.GOODS_DELETE_BINDING_BEAN_NAME)
	public Binding goodDeleteBinding() {
		return BindingBuilder.bind(goodsDeleteFanoutQueue()).to(goodsDeleteFanoutExchange());
	}

}
