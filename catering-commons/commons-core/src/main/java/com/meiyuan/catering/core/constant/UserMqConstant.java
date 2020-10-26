package com.meiyuan.catering.core.constant;

/**
 * @author lhm
 * @date 2020/3/26 11:11
 **/
public class UserMqConstant {

    /**
     * 新用户新增队列
     */
    public static final String USER_ADD_QUEUE_BEAN_NAME = "USER_ADD_QUEUE_BEAN";
    public static final String USER_ADD__QUEUE = "USER_ADD_QUEUE";


    /**
     * 用户广播路由bean名称
     */
    public static final String USER_BINDING_BEAN_NAME = "userBinding";

    /**
     * 新用户新增广播交换机
     */
    public static final String USER_ADD_FANOUT_EXCHANGE = "user.fanout.add.exchange";
    public static final String USER_ADD_FANOUT_EXCHANGE_BEAN_NAME = "userFanoutAddExchange";


}
