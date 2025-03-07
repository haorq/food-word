package com.meiyuan.catering.es.enums;

/**
 * @program: esdemo
 * @description: 分词器类型
 * @author: X-Pacific zhang
 * @create: 2019-01-29 09:54
 **/
public enum Analyzer {
    /**
     * 支持中文采用的方法为单字切分。他会将词汇单元转换成小写形式，并去除停用词和标点符号
     */
    standard,
    /**
     * 首先会通过非字母字符来分割文本信息，然后将词汇单元统一为小写形式。该分析器会去掉数字类型的字符
     */
    simple,
    /**
     * 仅仅是去除空格，对字符没有lowcase化,不支持中文
     */
    whitespace,
    /**
     * StopAnalyzer的功能超越了SimpleAnalyzer，在SimpleAnalyzer的基础上增加了去除英文中的常用单词（如the，a等）
     */
    stop,
    /**
     * 不分词，内容整体作为一个token(not_analyzed)
     */
    keyword,
    /**
     * 定义分隔符的正则表达式
     */
    pattern,
    /**
     *
     */
    fingerprint,
    /**
     * ik中文分词
     */
    ik_max_word,

    /**
     * 短语
     */
    ik_smart,
    /**
     * 拼音分词短语
     */
    ik_smart_pinyin
}
