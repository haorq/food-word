package com.meiyuan.catering.goods.config;

import com.meiyuan.catering.core.constant.GoodsMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * @author wxf
 */
@Configuration
public class GoodsRabbitMqConfig {

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
	 * 商户商品上下架广播信息交换机
	 *
	 * @author: wxf
	 * @date: 2020/3/23 14:46
	 * @return: {@link FanoutExchange}
	 **/
	@Bean(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_FANOUT_EXCHANGE_BEAN_NAME)
	public FanoutExchange merchantGoodsUpDownFanoutExchange() {
		return new FanoutExchange(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_FANOUT_EXCHANGE);
	}

	/**
	 * 商品交换机
	 *
	 * @return the exchange
	 */
	@Bean(GoodsMqConstant.GOODS_EXCHANGE_BEAN_NAME)
	public Exchange goodsDomainExchange() {
		Map<String, Object> args = new HashMap<>(1);
		return new DirectExchange(
				GoodsMqConstant.GOODS_EXCHANGE, true, false, args
		);
	}

	/**
	 * 商品/菜单推送队列
	 *
	 * @author: wxf
	 * @date: 2020/3/24 11:18
	 * @return: {@link Queue}
	 **/
	@Bean(GoodsMqConstant.GOODS_MENU_PUSH_QUEUE_BEAN_NAME)
	public Queue goodsMenuPushQueue() {
		return new Queue(GoodsMqConstant.GOODS_MENU_PUSH_QUEUE_NAME, true);
	}


	/**
	 * 商品/菜单推送队列绑定交换机路由
	 *
	 * @author: wxf
	 * @date: 2020/3/24 11:20
	 * @return: {@link Binding}
	 **/
	@Bean(GoodsMqConstant.GOODS_MENU_PUSH_BINDING_BEAN_NAME)
	public Binding goodsMenuPushBinding() {
		return BindingBuilder.bind(goodsMenuPushQueue()).to(goodsDomainExchange()).with(GoodsMqConstant.GOODS_MENU_PUSH_QUEUE_NAME).noargs();
	}

	/**
	 * 分类/标签删除队列
	 *
	 * @author: wxf
	 * @date: 2020/3/24 11:18
	 * @return: {@link Queue}
	 **/
	@Bean(GoodsMqConstant.CATEGORY_LABEL_DEL_QUEUE_BEAN_NAME)
	public Queue categoryLabelDelQueue() {
		return new Queue(GoodsMqConstant.CATEGORY_LABEL_DEL_QUEUE_NAME, true);
	}

	/**
	 * 分类/标签删除队列绑定交换机路由
	 *
	 * @author: wxf
	 * @date: 2020/3/24 11:20
	 * @return: {@link Binding}
	 **/
	@Bean(GoodsMqConstant.CATEGORY_LABEL_DEL_BINDING_BEAN_NAME)
	public Binding categoryLabelDelBinding() {
		return BindingBuilder.bind(categoryLabelDelQueue()).to(goodsDomainExchange()).with(GoodsMqConstant.CATEGORY_LABEL_DEL_QUEUE_NAME).noargs();
	}



	/**
	 * 商品新增修改队列
	 *
	 * @author: gz
	 * @date: 2020/3/23 17:55
	 * @return: {@link Queue}
	 * @version: 1.2.0
	 **/
	@Bean(GoodsMqConstant.PLATFORM_OR_MERCHANT_GOODS_UPDATE_QUEUE)
	public Queue platformOrMerchantGoodsUpdateQueue() {
		return new Queue(GoodsMqConstant.PLATFORM_OR_MERCHANT_GOODS_UPDATE_QUEUE, true);
	}

	@Bean
	public Binding platformOrMerchantGoodsUpdateBinding() {
		return BindingBuilder.bind(platformOrMerchantGoodsUpdateQueue()).to(goodsDomainExchange()).with(GoodsMqConstant.PLATFORM_OR_MERCHANT_GOODS_UPDATE_QUEUE).noargs();
	}
	/**
	 * 商品新增修改广播队列
	 *
	 * @author: gz
	 * @date: 2020/3/23 17:55
	 * @return: {@link Queue}
	 * @version: 1.2.0
	 **/
	@Bean(GoodsMqConstant.MERCHANT_GOODS_CATEGORY_UPDATE_QUEUE)
	public Queue merchantGoodsCategoryUpdateQueue() {
		return new Queue(GoodsMqConstant.MERCHANT_GOODS_CATEGORY_UPDATE_QUEUE, true);
	}

	@Bean
	public Binding merchantCategoryGoodsUpdateBinding() {
		return BindingBuilder.bind(merchantGoodsCategoryUpdateQueue()).to(goodsDomainExchange()).with(GoodsMqConstant.MERCHANT_GOODS_CATEGORY_UPDATE_QUEUE).noargs();
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
