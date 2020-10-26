package com.meiyuan.catering.core.constant;

/**
 * @author wxf
 * @date 2019/10/19
 * @description 商品MQ常量
 */
public class GoodsMqConstant {

    /**
     * 商品修改广播交换机
     */
    public static final String GOODS_ADD_UPDATE_FANOUT_EXCHANGE = "goods.fanout.addUpdate.exchange";
    public static final String GOODS_ADD_UPDATE_FANOUT_EXCHANGE_BEAN_NAME = "goodsFanoutAddUpdateExchange";
    /**
     * 添加/修改商品的广播队列bean名称
     */
    public static final String GOODS_ADD_UPDATE_FANOUT_QUEUE_BEAN_NAME = "goodsAddUpdateFanoutQueue";
    /**
     * 添加/修改商品的广播队列
     */
    public static final String GOODS_ADD_UPDATE_FANOUT_QUEUE = "GOODS_ADD_UPDATE_FANOUT_QUEUE";
    /** 购物车更新修改了信息的商品队列 */
//    public static final String GOODS_ADD_UPDATE_CART_QUEUE = "GOODS_ADD_UPDATE_CART_QUEUE";
    /**
     * 添加/修改商品广播路由bean名称
     */
    public static final String GOODS_BINDING_BEAN_NAME = "goodsBinding";

    /**
     * 商品交换机
     */
    public static final String GOODS_EXCHANGE_BEAN_NAME = "goodsDomainExchange";
    public static final String GOODS_EXCHANGE = "goodsDomainExchange";

//    /**
//     * 商品延迟交换机
//     */
//    public static final String GOODS_DELAYED_EXCHANGE_BEAN_NAME = "goodsDirectDelayExchange";
//    public static final String GOODS_DELAYED_EXCHANGE = "goodsDirectDelayExchange";

    /**
     * 商品/菜单推送队列bean名称
     */
    public static final String GOODS_MENU_PUSH_QUEUE_BEAN_NAME = "goodsMenuPushQueue";
    /**
     * 商品/菜单推送队列
     */
    public static final String GOODS_MENU_PUSH_QUEUE_NAME = "GOODS_MENU_PUSH_QUEUE_NAME";
    /**
     * 商品/菜单推送队列路由名称
     */
    public static final String GOODS_MENU_PUSH_BINDING_BEAN_NAME = "goodsMenuPushBinding";


    /**
     * 分类/标签删除队列bean名称
     */
    public static final String CATEGORY_LABEL_DEL_QUEUE_BEAN_NAME = "categoryLabelDelQueue";
    /**
     * 分类/标签删除队列
     */
    public static final String CATEGORY_LABEL_DEL_QUEUE_NAME = "CATEGORY_LABEL_DEL_QUEUE_NAME";
    /**
     * 分类/标签删除队列路由名称
     */
    public static final String CATEGORY_LABEL_DEL_BINDING_BEAN_NAME = "categoryLabelDelBinding";

    /**
     * 商品修改广播交换机
     */
    public static final String MERCHANT_GOODS_UP_DOWN_FANOUT_EXCHANGE = "merchant.goods.up.down.fanout.exchange";
    public static final String MERCHANT_GOODS_UP_DOWN_FANOUT_EXCHANGE_BEAN_NAME = "merchantGoodsUpDownFanoutExchange";
    /** 商品修改-用户购物车处理队列 */
//    public static  final String MERCHANT_GOODS_UP_DOWN_USER_CART_QUEUE = "MERCHANT_GOODS_UP_DOWN_USER_CART_QUEUE";
    /** 商户商品上下架修改-同步ES处理队列 */
    public static  final String MERCHANT_GOODS_UP_DOWN_ES_UPDATE_QUEUE_BEAN_NAME = "merchantGoodsUpDownEsUpdateQueue";
    /** 商户商品上下架修改-同步ES处理队列 */
    public static  final String MERCHANT_GOODS_UP_DOWN_ES_UPDATE_QUEUE = "MERCHANT_GOODS_UP_DOWN_ES_UPDATE_QUEUE";
    /**
     * 商户商品上下架修改-同步ES处理队列路由bean名称
     */
    public static final String MERCHANT_GOODS_UP_DOWN_ES_UPDATE_QUEUE_BINDING_BEAN_NAME = "merchantGoodsUpDownEsUpdateQueueBinding";
//    /**
//     * 修改菜单队列
//     */
//    public static final String UPDATE_MENU_QUEUE = "UPDATE_MENU_QUEUE";
//    public static final String UPDATE_MENU_QUEUE_BEAN_NAME = "updateMenuQueue";
//
//    /**
//     * 修改菜单队列绑定路由
//     */
//    public static final String UPDATE_MENU_QUEUE_BINDING_BEAN_NAME = "updateMenuQueueBinding";

//    /**
//     * 商品结束售卖时间队列
//     */
//    public static final String GOODS_END_SELL_TIME_QUEUE = "GOODS_END_SELL_TIME_QUEUE";
//    public static final String GOODS_END_SELL_TIME_QUEUE_BEAN_NAME = "goodsEndSellTimeQueue";

//    /**
//     * 商品结束售卖时间队列绑定路由
//     */
//    public static final String GOODS_END_SELL_TIME_QUEUE_BINDING_BEAN_NAME = "goodsEndSellTimeQueueBinding";

    /**
     * 平台/商户商品修改队列 v1.2.0
     */
    public static final String PLATFORM_OR_MERCHANT_GOODS_UPDATE_QUEUE = "PLATFORM_OR_MERCHANT_GOODS_UPDATE_QUEUE";
    /**
     * 平台/商户商品修改队列 v1.2.0
     */
    public static final String MERCHANT_GOODS_CATEGORY_UPDATE_QUEUE = "MERCHANT_GOODS_CATEGORY_UPDATE_QUEUE";


    /**
     * 商品删除、取消授权广播交换机
     */
    public static final String GOODS_DELETE_FANOUT_EXCHANGE = "goods.fanout.delete.exchange";
    public static final String GOODS_DELETE_FANOUT_EXCHANGE_BEAN_NAME = "goodsFanoutDeleteExchange";


    /**
     * 商品删除、取消授权广播队列bean名称
     */
    public static final String GOODS_DELETE_FANOUT_QUEUE_BEAN_NAME = "goodsDeleteFanoutQueue";


    /**
     * 商品删除、取消授权
     */
    public static final String GOODS_DELETE_FANOUT_QUEUE = "GOODS_DELETE_FANOUT_QUEUE";

    /**
     * 添加/修改商品广播路由bean名称
     */
    public static final String GOODS_DELETE_BINDING_BEAN_NAME = "goodsDeleteBinding";
}


