package com.meiyuan.catering.core.redis;

/**
 * @author yaoozu
 * @description
 * @date 2020/2/2615:55
 * @since v1.0.0
 */
public class JetcacheNames {
    /**
     * 验证码
     */
    public final static String CAPTCHA_CODE = "captcha:code:";
    /**
     * 重复提交
     */
    public final static String REPEAT_SUBMIT = "admin:repeat_submit:";

    /**
     * 短信验证码
     */
    public final static String ADMIN_PSW_CODE = "user:psw:code";
    /**
     * web端TOKEN
     */
    public final static String ADMIN_TOKEN = "admin:token:";


    public final static String PUSHER_CODE = "pusher:code:";

    /**
     * 验证码
     */
    public final static String USER_TOKEN = "user:token:";
    public final static String USER_USERID_TOKEN = "user:userId:token:";
    public final static String USER_USERID_LOGDIN_DATA = "user:userId:login:data";

    /**
     * 系统日志
     */
    public static final String SYS_LOG_KEY = "admin:sysLog:";
    /**
     * 行政区编码
     */
    public static final String SYS_REGION_KEY = "admin:region:";

    /**
     * 商品编码
     */
    public static final String GOODS_CODE_KEY = "goods:code:";
    /**
     * 商户商品编码
     */
    public static final String MERCHAT_GOODS_CODE_KEY = "merchant:goods:code:";
    /**
     * 订单取餐码
     */
    public static final String ORDER_TAKE_CODE_KEY = "order:take:code:";

    /**
     * 字典 广告跳转页面
     */
    public static final String ADMIN_ADVERTISING_URL = "admin:advertising:url:";
    /**
     * 缓存所有字典
     */
    public static final String ADMIN_DIC = "admin:dic:code:";

    /**
     * 缓存所有企业用户状态
     */
    public static final String USER_COMPANY_STATUS = "user:company:status:";

    /**
     * 商户基本信息
     */
    public static final String MERCHANT_INFO = "merchant:info:";

    /**
     * 店铺基本信息
     */
    public static final String SHOP_CODE_KEY = "shop:code:";
    /**
     * 店铺基本信息
     */
    public static final String SHOP_INFO = "shop:info:";

    /**
     * 店铺基本信息
     */
    public static final String SHOP_CONFIG_INFO = "shop:config:info:";

    /**
     * 店铺标签信息
     */
    public static final String SHOP_TAG_INFO = "shop:tag:info:";
    /**
     * 店铺地址相关缓存
     */
    public static final String SHOP_CITY_INFO = "shop:city:info:";
    /**
     * app-店铺新订单通知缓存
     */
    public static final String SHOP_ORDER_NOTICE = "shop:order:notice:";

    /**
     * app-店铺订单补贴
     */
    public static final String SHOP_ORDER_SUBSIDY = "shop:order:subsidy:";
    /**
     * app-店铺新订单通知缓存
     */
    public static final String DEVICE_NOTICE_QUEUE = "device:notice:queue:";
    /**
     * pc端账号登录状态记录（启用、禁用）
     */
    public static final String ACCOUNT_STATUS = "account:status:";




    /**
     * 广告redis key
     */
    public static final String REDIS_ADVERTISING_KEY = "admin:advertising:";
    /**
     * 小程序类目redis key
     */
    public static final String REDIS_WX_CATEGORY_KEY = "admin:wxCategory:";
    /**
     * 微信优惠券弹窗 redis key
     */
    public static final String TICKET_DIALOG_KEY = "marketing:ticket:dialog:";
    /**
     * 微信秒杀弹窗 redis key
     */
    public static final String SECKILL_DIALOG_KEY = "marketing:seckill:dialog:";

    public static final String COUPON_DIALOG_KEY = "marketing:coupon:dialog:";

    /**
     * 秒杀相关 start
     */
    public static final String SECKILL_INVENTORY = "seckill:inventory:";
    public static final String SECKILL_GOODS = "seckill:goods:";
    public static final String SECKILL_USER_HAVE_BOUGHT = "seckill:user_have_bought:";
    public static final String SECKILL_LOCK = "seckill:lock:";
    public static final String SECKILL_SOLDOUT = "seckill:sold_out:";
    public static final String SECKILL_CART = "seckill:cart:";
    /** 秒杀相关 end*/


    /**
     * 商户评价、订单统计
     */
    public static final String MERCHANT_COUNT = "merchant:count:";

    /**
     * 限制
     */
    public final static String RESTRICT_CODE = "restrict:code:";

    /**
     * 订单配置
     */
    public final static String ORDER_CONFIG = "order:config:";

    /**
     * 团购相关
     */
    public static final String GROUPON_SOLDOUT = "groupon:sold_out:";

    public static final String GROUPON_LOCK = "groupon:lock:";

    /**
     * 支付锁
     */
    public static final String PAY_LOCK = "pay:lock:";
    /**
     * 退款锁
     */
    public static final String REFUND_LOCK = "refund:lock:";
    /**
     * 老系统信息处理锁
     */
    public static final String OLD_SYSTEM_INFO_LOCK = "oldSystemInfo:lock:";

    public static final String GROUND_PUSHER_MSG = "user:ground_pusher_msg:";

    /**
     * @description 创建了拼单标识
     * @author yaozou
     * @date 2020/5/11 10:41
     * @param
     * @return
     * @since v1.0.0
     */
    public static final String CREATE_SHAREBILL_FLAG = "create:sharebill:flag";


    /**商户PC端营销活动【发现新活动】标识*/
    public static final String MERCHANT_PC_NEW_ACTIVITY_FLAG = "pc:new_activity_flag:";
}
