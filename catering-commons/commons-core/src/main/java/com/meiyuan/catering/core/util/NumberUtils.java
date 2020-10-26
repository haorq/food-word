package com.meiyuan.catering.core.util;

import java.math.BigDecimal;

/**
 * @author yaoozu
 * @description 数字工具
 * @date 2020/3/3018:52
 * @since v1.0.0
 */
public class NumberUtils {
    public static BigDecimal doubleToDecimal(double d){
        return BigDecimal.valueOf(d);
    }
    public static Double     decimalToDouble(BigDecimal b){
        return b.doubleValue();
    }
    public static BigDecimal   setScale(BigDecimal b){
        return b.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
