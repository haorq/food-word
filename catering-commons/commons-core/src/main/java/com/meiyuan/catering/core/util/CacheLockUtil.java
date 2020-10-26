package com.meiyuan.catering.core.util;

/**
 * 缓存锁工具
 *
 * @author zengzhangni
 * @date 2020/3/30
 */
public class CacheLockUtil {

    private final static String ORDER_PAY_NOTIFY = "orderPay-";
    private final static String WX_PAY_NOTIFY = "wxPay-";
    private final static String RECHARGE_PAY_NOTIFY = "rechargePay-";
    private final static String RECHARGE_PAY_REQUEST = "rechargeRequest-";
    private final static String ORDER_REFUND = "orderRefund-";
    private final static String BALANCE_PAY = "balancePay-";
    private final static String APPLY_REFUND = "applyRefund-";
    private final static String ORDER_CANCEL = "orderCancel-";
    private final static String CANCEL_GROUP = "cancelGroup-";
    private final static String ORDER_SUBMIT = "orderSubmit-";
    private final static String SYNC_SECKILL = "syncSeckill-";
    private final static String GIFT_GOODS = "giftGoods-";
    private final static String ORDER_COMMENT = "orderComment-";
    private final static String GROUPON_END = "grouponEnd-";
    private final static String SPECIAL_BEGIN_END = "specialBeginEnd-";
    public final static Long EXPIRE = 10L;


    public static String orderPayNotifyLock(Object key) {
        return ORDER_PAY_NOTIFY + key;
    }

    public static String wxPayLock(Object key) {
        return WX_PAY_NOTIFY + key;
    }

    public static String rechargePayNotifyLock(Object key) {
        return RECHARGE_PAY_NOTIFY + key;
    }

    public static String orderRefundLock(Object key) {
        return ORDER_REFUND + key;
    }

    public static String orderCancelLock(Object key) {
        return ORDER_CANCEL + key;
    }

    public static String cancelGroupLock(Object key) {
        return CANCEL_GROUP + key;
    }

    public static String balancePayLock(Object key) {
        return BALANCE_PAY + key;
    }

    public static String rechargeWxPayLock(Object key, Object key2) {
        return RECHARGE_PAY_REQUEST + key + key2;
    }

    public static String applyRefundLock(Object key, Object key2) {
        return APPLY_REFUND + key + key2;
    }

    public static String orderCommitLock(Object key) {
        return ORDER_SUBMIT + key;
    }

    public static String syncSeckillLock(Object key) {
        return SYNC_SECKILL + key;
    }

    public static String giftGoodsLock(Object key) {
        return GIFT_GOODS + key;
    }

    public static String orderCommentLock(Object key) {
        return ORDER_COMMENT + key;
    }

    public static String grouponEnd(Object key) {
        return GROUPON_END + key;
    }

    public static String specialBeginEnd(Object key) {
        return SPECIAL_BEGIN_END + key;
    }
}
