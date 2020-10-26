package com.meiyuan.catering.core.exception;

/**
 * @author admin
 */
public interface ErrorCode {
    /*========================商户相关错误码定义======================*/

    int INTERNAL_SERVER_ERROR = 500;
    String INTERNAL_SERVER_ERROR_MSG = "服务器或网络不稳定，请稍后再试";
    /**
     * 商户模块：商户枚举类型状态值不存在
     */
    int MERCHANT_ENUM_STATUS = 15025;
    /**
     * 用户模块：用户枚举类型状态值不存在
     */
    int USER_ENUM_STATUS = 14009;

    /**
     * 店铺信息重复
     */
    int SHOP_INFO_REPEAT = 501;

    /**
     * 打印机已在当前店铺添加
     */
    int PRINT_ADD_ERROR = 10001;
    String PRINT_ADD_ERROR_MSG = "打印机已在当前店铺添加";


    /*========================商户相关错误码定义======================*/

    /** 秒杀 */
    /**
     * 已抢光
     */
    int SECKILL_SOLD_OUT_ERROR = 12001;
    /**
     * 库存不足
     */
    int SECKILL_INVERSTORY_EMTRY_ERROR = 12002;
    /**
     * 存在秒杀购物车标识
     */
    int SECKILL_CART_FLAG_ERROR = 12003;
    /**
     * 秒杀下架提示标识
     */
    int SECKILL_DOWN_ERROR = 12004;
    String SECKILL_DOWN_ERROR_MSG = "秒杀活动已下架";
    /**
     * 秒杀拼单标识
     */
    int SECKILL_SHARE_BILL_ERROR = 12005;

    /**
     * 营销特价商品选择为空标志
     */
    int SPECIAL_GOODS_EMPTY_ERROR = 12006;
    String SPECIAL_GOODS_EMPTY_ERROR_MSG = "保存失败！设置商品未在售卖菜单中";
    /**
     * 门店销售菜单查询为空标识
     */
    int SPECIAL_GOODS_SELECT_EMPTY_ERROR = 12007;
    String SPECIAL_GOODS_SELECT_EMPTY_ERROR_MSG = "保存失败！售卖菜单中无商品信息";

    /** ===== 添加购物车=======**/

    /**
     * 添加购物车时 商品已下架
     */
    int CART_GOODS_LOWER_SHELF_ERROR = 20001;
    /**
     * 商品价格、规格、商户售卖模式发生了变化
     */
    int CART_GOODS_CHANGED_ERROR = 20002;
    /**
     * 添加时，商品数量为负数的错误
     */
    int CART_GOODS_NUMBER_ERROR = 20003;


    /**==== 拼单 =====**/

    /**
     * 发起人 结算中 未生成订单 控制前端跳转到结算页
     */
    int SHARE_BILL_PAYING_ERROR = 21001;
    /**
     * 拼单已经结束 不可操作拼单，不可查看拼单详情，但可通过发起人可以通过订单-订单详情
     */
    int SHARE_BILL_FINISHED_ERROR = 21002;
    /**
     * 发起人 已结算  生成订单（支付成功、未支付）  控制前端跳转到订单详情页，操作逻辑和其他订单一致
     */
    int SHARE_BILL_PAYED_ERROR = 21003;
    /**
     * 用户状态不正确 企业用户不可进行拼单
     */
    int SHARE_BILL_USER_TYPE_ERROR = 21004;
    /**
     * 拼单状态不是选购中时，控制前端跳转到 拼单-拼单人的详情页
     */
    int SHARE_BILL_PAYING_BILL_USER_ERROR = 21005;
    int SHARE_BILL_PAYED_BILL_USER_ERROR = 21006;
    int CAN_NOT_CONTINUE_SHARE_ERROR = 21007;

    /**===== 地推员 =====*/

    /**
     * 不存在可以领取的优惠券--限老用户
     */
    int GROUND_PUSHER_NO_TICKET = 31001;

    /**===== 订单 =====*/

    /**
     * 订单评价异常
     */
    int ORDER_APPRAISE_ERROE = 32001;
    /**
     * 订单结算，商品下架
     */
    int ORDER_GOODS_DOWN = 32002;
    /**
     * 订单结算，商品改价
     */
    int ORDER_GOODS_PRICE_CHANGE = 32003;
    /**
     * 订单支付超时
     */
    int ORDER_PAY_TIMEOUT = 32005;


    int ORDER_MARKETING_GOODS_NOT_EXIST = 32004;
    String ORDER_MARKETING_GOODS_NOT_EXIST_MSG = "活动商品不存在";


    /**===== 店铺小程序是否可下单 =====*/
    /**
     * 店铺启用禁用状态
     */
    int SHOP_STATUS_ERROR = 40001;
    String SHOP_STATUS_MSG = "门店去流浪了~";

    int SHOP_BUSINESS_STATUS_ERROR = 40002;
    String SHOP_BUSINESS_STATUS_MSG = "店铺已经打烊啦~";

    int SHOP_SERVICE_ERROR = 40003;
    String SHOP_SERVICE_MSG = "店铺服务类型仅为堂食wx端不可下单";

    int MERCHANT_STATUS_ERROR = 40004;
    String MERCHANT_STATUS_MSG = "门店去流浪了~";

    int MERCHANT_AUDIT_STATUS_ERROR = 40005;
    String MERCHANT_AUDIT_STATUS_MSG = "门店去流浪了~";

    int NOT_TAKEOUT_GOODS_ERROR = 40006;

    /**
     * @since v1.2.0 购物车商品检查提示code
     */
    int CART_CHECK_ERROR = 50000;


    /**
     * 团购
     *
     * @version 1.2.0
     */
    int GROUPON_DOWN = 60000;
    String GROUPON_DOWN_MSG = "活动已经结束啦~";


    /**
     * pc端地址跳转基本信息获取失败
     */
    int INFO_FAILURE_ERROR = 60001;


    /**
     * 超出自取时间段
     */
    int SELF_PICKUP_TIMEOUT_ERROR = 70000;
    String SELF_PICKUP_TIMEOUT_ERROR_MSG = "已经超出自取时间啦，请明天再来~";
    /**
     * 超出外卖配送时间段
     */
    int TAKEOUT_TIMEOUT_ERROR = 70001;
    String TAKEOUT_TIMEOUT_ERROR_MSG = "已经超出配送时间啦，请明天再来~";


    int TICKET_HAS_SHOP_ERROR = 80001;
    String TICKET_HAS_SHOP_ERROR_MSG = "已选门店在活动时间范围内已有活动";
    int TICKET_HAS_NAME_ERROR = 80002;
    String TICKET_HAS_NAME_ERROR_MSG = "活动名称已存在,请修改";


    int TICKET_ACTIVITY_END_ERROR = 80003;
    int TICKET_ACTIVITY_LIMIT_ERROR = 80004;
    int TICKET_ACTIVITY_MSM_CODE_ERROR = 80005;


    /**
     * 活动冻结提示
     */
    String ACTIVITY_FREEZE_ERROR_MSG = "活动已经结束啦~";


    /**
     * 打印错误
     */

    int PRINT_ERROR = 90000;
    String PRINT_ERROR_MSG = "打印失败";


    String SHOP_SIGN_STATUS_FAIL_MSG = "结算信息未完善";
}
