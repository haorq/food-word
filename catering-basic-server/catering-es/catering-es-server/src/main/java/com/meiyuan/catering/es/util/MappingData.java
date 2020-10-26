package com.meiyuan.catering.es.util;

import lombok.Data;

/**
 * @program: esdemo
 * @description: mapping注解对应的数据载体类
 * @author: X-Pacific zhang
 * @create: 2019-01-29 15:09
 **/
@Data
public class MappingData {
    String fieldName;
    /**
     * 数据类型（包含 关键字类型）
     */
    String datatype;
    /**
     * 间接关键字
     */
    boolean keyword;
    /**
     * 关键字忽略字数
     */
    int ignoreAbove;
    /**
     * 是否支持autocomplete，高效全文搜索提示
     */
    boolean autocomplete;
    /**
     * 是否支持suggest，高效前缀搜索提示
     */
    boolean suggest;
    /**
     * 索引分词器设置
     */
    String analyzer;
    /**
     * 搜索内容分词器设置
     */
    String searchAnalyzer;

    boolean includeInParent;
    private boolean allowSearch;
    private String copyTo;
}
