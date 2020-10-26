package com.meiyuan.catering.core.constant;

/**
 * @Author MeiTao
 * @Description 商户相关消息队列常量
 * @Date  2020/4/7 0007 16:12
 */
public class MerchantMqConstant {

    //========================售卖模式修改相关常量===============================
    /**
     * 商户修改售卖模式
     */
//    public static final String MERCHANT_UPDATE_QUEUE_BEAN_NAME = "MERCHANT_UPDATE_QUEUE_BEAN";
//    public static final String MERCHANT_UPDATE_QUEUE = "MERCHANT_UPDATE_QUEUE";


    /**
     * 商户广播路由bean名称
     */
    public static final String MERCHANT_BINDING_BEAN_NAME = "merchantBinding";

    /**
     * 商户广播交换机
     */
    public static final String MERCHANT_UPDATE_FANOUT_EXCHANGE = "merchant.fanout.update.exchange";
    public static final String MERCHANT_UPDATE_FANOUT_EXCHANGE_BEAN_NAME = "merchantFanoutUpdateExchange";

    //========================商户基本信息修改消 息队列相关常量===============================
    /**
     * 商户基本信息修改队列
     */
    public static final String MERCHANT_INFO_QUEUE = "MERCHANT_INFO_QUEUE";
    public static final String MERCHANT_INFO_QUEUE_BEAN = "merchantInfoQueue";

    /**
     * 商户基本信息修改交换机
     */
    public static final String MERCHANT_INFO_FANOUT_EXCHANGE = "merchant.info.fanout.exchange";
    public static final String MERCHANT_INFO_FANOUT_EXCHANGE_BEAN = "merchantInfoFanoutExchange";

    /**
     * 商户基本信息修改路由
     */
    public static final String MERCHANT_INFO_FANOUT_EXCHANGE_ROUTING = "MERCHANT_INFO_FANOUT_EXCHANGE_ROUTING";

    public static final String MERCHANT_INFO_BINDING_BEAN = "merchantInfoBinding";

    /**
     * 商户登陆名前缀
     */
    public static final String MERCHANT_LOGIN_PREFIX = "XS";
}
