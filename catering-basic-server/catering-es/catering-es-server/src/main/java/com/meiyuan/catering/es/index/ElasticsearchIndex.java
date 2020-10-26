package com.meiyuan.catering.es.index;

/**
 * @program: esdemo
 * @description: 索引结构基础方法接口
 * @author: X-Pacific zhang
 * @create: 2019-01-25 16:52
 **/
public interface ElasticsearchIndex<T> {
    /**
     * 创建索引
     *
     * @author: wxf
     * @date: 2020/6/23 14:12
     * @param clazz clazz
     * @throws Exception 抛出异常
     * @version 1.1.0
     **/
    void createIndex(Class<T> clazz) throws Exception;
    /**
     * 删除索引
     *
     * @author: wxf
     * @date: 2020/6/23 14:12
     * @param clazz clazz
     * @throws Exception 抛出异常
     * @version 1.1.0
     **/
    void dropIndex(Class<T> clazz) throws Exception;

    /**
     * 索引是否存在
     *
     * @author: wxf
     * @date: 2020/6/23 14:12
     * @param clazz clazz
     * @throws Exception 抛出异常
     * @return: {@link boolean}
     * @version 1.1.0
     **/
    boolean exists(Class<T> clazz) throws Exception;

}
