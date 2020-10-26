package com.meiyuan.catering.core.util;

import java.math.BigDecimal;

/**
 * 优惠信息
 *
 * @author zengzhangni
 * @date 2020/9/4 10:12
 * @since v1.1.0
 */
public class DiscountInfo {

    private final static String GROUPON_DISCOUNT_MSG = "团购商品%s元起";
    private final static String SECKILL_DISCOUNT_MSG = "秒杀商品%s元起";
    private final static String GOODS_DISCOUNT_MSG = "折扣商品%s折起";
    private final static String GOODS_DISCOUNT_SIMPLE_MSG = "%s折起";
    private final static String GET_TICKET_MSG = "领%s元券";
    private final static String SEND_TICKET_MSG = "%s元券";
    private final static String SEND_TICKET_SUBTRACT_MSG = "满%s减%s";
    private final static String DISPATCHING_DISCOUNT_MSG = "满%s元免配送费";


    /**
     * 描述: 团购商品x元起
     *
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/9/4 10:46
     * @since v1.4.0
     */
    public static String grouponMsg(Object o) {
        return String.format(GROUPON_DISCOUNT_MSG, o);
    }

    public static String grouponMsg(Double o) {
        return grouponMsg(BaseUtil.toPlainString(o));
    }

    /**
     * 描述: 秒杀商品x元起
     *
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/9/4 10:46
     * @since v1.4.0
     */
    public static String seckillMsg(Object o) {
        return String.format(SECKILL_DISCOUNT_MSG, o);
    }

    public static String seckillMsg(Double o) {
        return seckillMsg(BaseUtil.toPlainString(o));
    }

    /**
     * 描述: 折扣商品x折起
     *
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/9/4 10:46
     * @since v1.4.0
     */
    public static String goodsDiscountMsg(Object o) {
        return String.format(GOODS_DISCOUNT_MSG, o);
    }
    public static String goodsDiscountMsg(Double o) {
        return goodsDiscountMsg(BaseUtil.toPlainString(o));
    }

    public static String goodsDiscountMsg(BigDecimal o) {
        return goodsDiscountMsg(BaseUtil.toPlainString(o));
    }

    /**
     * 描述: x折起
     *
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/9/4 10:46
     * @since v1.4.0
     */
    public static String goodsDiscountSimpleMsg(Object o) {
        return String.format(GOODS_DISCOUNT_SIMPLE_MSG, o);
    }

    public static String goodsDiscountSimpleMsg(Double o) {
        return goodsDiscountSimpleMsg(BaseUtil.toPlainString(o));
    }


    /**
     * 描述: 领x元劵
     *
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/9/4 10:46
     * @since v1.4.0
     */
    public static String getTicketMsg(Object o) {
        return String.format(GET_TICKET_MSG, o);
    }

    public static String getTicketMsg(BigDecimal b) {
        return getTicketMsg(BaseUtil.toPlainString(b));
    }

    /**
     * 描述: x元劵
     *
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/9/4 10:46
     * @since v1.4.0
     */
    public static String sendTicketMsg(Object o) {
        return String.format(SEND_TICKET_MSG, o);
    }

    public static String sendTicketMsg(BigDecimal b) {
        return sendTicketMsg(BaseUtil.toPlainString(b));
    }

    /**
     * 描述: 满x减x
     *
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/9/4 10:46
     * @since v1.4.0
     */
    public static String sendTicketSubtractMsg(Object o1, Object o2) {
        return String.format(SEND_TICKET_SUBTRACT_MSG, o1, o2);
    }

    public static String sendTicketSubtractMsg(BigDecimal b1, BigDecimal b2) {
        return sendTicketSubtractMsg(BaseUtil.toPlainString(b1), BaseUtil.toPlainString(b2));
    }

    /**
     * 描述: 满x免配送费
     *
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/9/4 10:46
     * @since v1.4.0
     */
    public static String dispatchingDiscountMsg(Object o) {
        return String.format(DISPATCHING_DISCOUNT_MSG, o);
    }

    public static String dispatchingDiscountMsg(BigDecimal b1) {
        return dispatchingDiscountMsg(BaseUtil.toPlainString(b1));
    }

}
