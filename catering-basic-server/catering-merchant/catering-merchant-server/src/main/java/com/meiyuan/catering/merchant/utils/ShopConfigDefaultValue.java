package com.meiyuan.catering.merchant.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Description 店铺配送默认值
 * @Date  2020/3/12 0012 10:27
 */
public class ShopConfigDefaultValue {
    /**
     * 配送范围：米（店铺默认配送范围）
     */
    public final static Integer DELIVERY_RANGE = 1000;

    /** 配送价格 */
    public static final BigDecimal DELIVERY_PRICE = new BigDecimal("0");

    /** 配送开始时间 */
    public static final String START_TIME = "08:00";

    /** 配送开始时间 */
    public static final String END_TIME = "19:59";
}
