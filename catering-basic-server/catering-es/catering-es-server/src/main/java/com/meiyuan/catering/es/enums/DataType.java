package com.meiyuan.catering.es.enums;

/**
 * @program: esdemo
 * @description: es字段数据结构
 * @author: X-Pacific zhang
 * @create: 2019-01-25 16:58
 **/
public enum DataType {
    /**
     * 关键字
     */
    keyword_type,
    /**
     * 文本
     */
    text_type,
    /**
     * 有符号的8位整数, 范围: [-128 ~ 127]
     */
    byte_type,
    /**
     * 有符号的16位整数, 范围: [-32768 ~ 32767]
     */
    short_type,
    /**
     * 有符号的32位整数, 范围: [$-2^{31}$ ~ $2^{31}$-1]
     */
    integer_type,
    /**
     * 有符号的32位整数, 范围: [$-2^{63}$ ~ $2^{63}$-1]
     */
    long_type,
    /**
     * 32位单精度浮点数
     */
    float_type,
    /**
     * 64位双精度浮点数
     */
    double_type,
    /**
     * 16位半精度IEEE 754浮点类型
     */
    boolean_type,
    /**
     * 日期类型
     */
    date_type,
    /**
     * 嵌套类型
     */
    nested,
    /**
     * 对象
     */
    object,
    /**
     * 经纬度
     */
    geo_point;
}
