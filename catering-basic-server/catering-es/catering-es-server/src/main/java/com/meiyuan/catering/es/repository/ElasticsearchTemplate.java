package com.meiyuan.catering.es.repository;


import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.es.dto.geo.EsGeoSearchDTO;
import com.meiyuan.catering.es.enums.AggsType;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @program: esdemo
 * @description: Elasticsearch基础功能组件
 * @author: X-Pacific zhang
 * @create: 2019-01-18 16:01
 **/
public interface ElasticsearchTemplate<T, M> {
    /**
     * 通过Low Level REST Client 查询
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.6/java-rest-low-usage-requests.html
     *
     * @param request
     * @return
     * @throws Exception
     */
    Response request(Request request) throws Exception;

    /**
     * 新增索引
     *
     * @param t 保存类型
     * @throws Exception 抛出异常
     * @author: wxf
     * @date: 2020/6/23 14:21
     * @return: {@link boolean}
     * @version 1.1.0
     **/
    boolean save(T t) throws Exception;

    /**
     * 新增索引集合
     *
     * @param list 保存类型集合
     * @throws Exception 抛出异常
     * @author: wxf
     * @date: 2020/6/23 14:21
     * @return: {@link BulkResponse}
     * @version 1.1.0
     **/
    BulkResponse save(List<T> list) throws Exception;

    /**
     * 按照有值字段更新索引
     *
     * @param t 保存类型
     * @throws Exception 抛出异常
     * @author: wxf
     * @date: 2020/6/23 14:21
     * @return: {@link boolean}
     * @version 1.1.0
     **/
    boolean update(T t) throws Exception;

    /**
     * 覆盖更新索引
     *
     * @param t 保存类型
     * @throws Exception 抛出异常
     * @author: wxf
     * @date: 2020/6/23 14:21
     * @return: {@link boolean}
     * @version 1.1.0
     **/
    boolean updateCover(T t) throws Exception;

    /**
     * 删除索引
     *
     * @param t 类型
     * @throws Exception 抛出异常
     * @author: wxf
     * @date: 2020/6/23 14:21
     * @return: {@link boolean}
     * @version 1.1.0
     **/
    boolean delete(T t) throws Exception;

    /**
     * 批量删除
     *
     * @param queryBuilder 查询条件
     * @param clazz        类型
     * @throws Exception 抛出异常
     * @author: wxf
     * @date: 2020/6/23 14:45
     * @return: {@link BulkByScrollResponse}
     * @version 1.1.0
     **/
    BulkByScrollResponse deleteByCondition(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 删除索引
     *
     * @param id    id
     * @param clazz clazz
     * @throws Exception 抛出异常
     * @author: wxf
     * @date: 2020/6/23 15:04
     * @return: {@link boolean}
     * @version 1.1.0
     **/
    boolean deleteById(M id, Class<T> clazz) throws Exception;

    /**
     * 【最原始】查询
     *
     * @param searchRequest searchRequest
     * @throws Exception 抛出异常
     * @author: wxf
     * @date: 2020/6/23 15:04
     * @return: {@link SearchResponse}
     * @version 1.1.0
     **/
    SearchResponse search(SearchRequest searchRequest) throws Exception;

    /**
     * 非分页查询
     * 目前暂时传入类类型
     *
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @throws Exception 抛出异常
     * @author: wxf
     * @date: 2020/6/23 15:05
     * @return: {@link List<T>}
     * @version 1.1.0
     **/
    List<T> search(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 非分页查询(跨索引)
     * 目前暂时传入类类型
     *
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @param indexs       索引数组
     * @return {@link List<T>}
     * @throws Exception 抛出异常
     */
    List<T> search(QueryBuilder queryBuilder, Class<T> clazz, String... indexs) throws Exception;

    /**
     * 自定义分页查询
     * @param queryBuilder 查询条件
     * @param clazz clazz
     * @param pageNum 当前页
     * @param pageSize 每页查询条数
     * @param indexs 索引数组
     * @return {@link List<T>}
     * @throws Exception 异常
     */
    List<T> search(QueryBuilder queryBuilder, Class<T> clazz, Integer pageNum, Integer pageSize, String... indexs) throws Exception;

    List<T> search(QueryBuilder queryBuilder, Class<T> clazz, Attach attach) throws Exception;

    /**
     * 查询数量
     *
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @return {@link long}
     * @throws Exception 抛出异常
     */
    long count(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;


    /**
     * 查询数量(跨索引)
     *
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @param indexs       索引数组
     * @return {@link long}
     * @throws Exception 抛出异常
     */
    long count(QueryBuilder queryBuilder, Class<T> clazz, String... indexs) throws Exception;

    /**
     * 支持分页、高亮、排序的查询
     *
     * @param queryBuilder      查询条件
     * @param pageSortHighLight 分页查询参数
     * @param clazz             clazz
     * @param index             索引
     * @return {@link PageData}
     * @throws Exception 抛出异常
     */
    PageData<T> search(QueryBuilder queryBuilder, PageSortHighLight pageSortHighLight, Class<T> clazz, String index) throws Exception;


    /**
     * 支持分页、高亮、排序的查询（跨索引）
     *
     * @param queryBuilder      查询条件
     * @param pageSortHighLight 分页查询参数
     * @param clazz             clazz
     * @param indexs            索引数组
     * @return {@link PageData}
     * @throws Exception 抛出异常
     */
    PageData<T> search(QueryBuilder queryBuilder, PageSortHighLight pageSortHighLight, Class<T> clazz, String... indexs) throws Exception;


    /**
     * scroll方式查询(默认了保留时间为Constant.DEFAULT_SCROLL_TIME)
     * //TODO 添加跨索引
     *
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @return {@link List<T>}
     * @throws Exception 抛出异常
     */
    List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * scroll方式查询
     * //TODO 添加跨索引
     *
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @param time         保留小时
     * @return {@link List<T>}
     * @throws Exception 抛出异常
     */
    List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz, long time) throws Exception;

    /**
     * Template方式搜索，Template已经保存在script目录下
     * look at https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.6/java-search-template.html
     * 暂时无法使用该方法，原因为官方API SearchTemplateRequestBuilder仍保留对transportClient 的依赖，但Migration Guide 中描述需要把transportClient迁移为RestHighLevelClient
     *
     * @param templateParams 模板参数
     * @param templateName   模板名称
     * @param clazz          clazz
     * @return {@link List<T>}
     * @throws Exception 抛出异常
     */
    List<T> searchTemplate(Map<String, Object> templateParams, String templateName, Class<T> clazz) throws Exception;

    /**
     * Template方式搜索，Template内容以参数方式传入
     * look at https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.6/java-search-template.html
     * 暂时无法使用该方法，原因为官方API SearchTemplateRequestBuilder仍保留对transportClient 的依赖，但Migration Guide 中描述需要把transportClient迁移为RestHighLevelClient
     *
     * @param templateParams 模板参数
     * @param templateSource 模板源
     * @param clazz          clazz
     * @return {@link List<T>}
     * @throws Exception 抛出异常
     */
    List<T> searchTemplateBySource(Map<String, Object> templateParams, String templateSource, Class<T> clazz) throws Exception;

    /**
     * 保存Template
     * look at https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.6/java-search-template.html
     * 暂时无法使用该方法，原因为官方API SearchTemplateRequestBuilder仍保留对transportClient 的依赖，但Migration Guide 中描述需要把transportClient迁移为RestHighLevelClient
     *
     * @param templateName   模板名称
     * @param templateSource 模板源
     * @param clazz          clazz
     * @return {@link List<T>}
     * @throws Exception 抛出异常
     */
    List<T> saveTemplate(String templateName, String templateSource, Class<T> clazz) throws Exception;

    /**
     * 搜索建议
     * //TODO 添加跨索引
     *
     * @param fieldName  字段名称
     * @param fieldValue 字段值
     * @param clazz      clazz
     * @return {@link List<String>}
     * @throws Exception 抛出异常
     */
    List<String> completionSuggest(String fieldName, String fieldValue, Class<T> clazz) throws Exception;

    /**
     * 根据ID查询
     *
     * @param id    id
     * @param clazz clazz
     * @return {@link T}
     * @throws Exception 抛出异常
     */
    T getById(M id, Class<T> clazz) throws Exception;

    /**
     * 根据ID列表批量查询
     *
     * @param ids   id数组
     * @param clazz clazz
     * @return {@link List<T>}
     * @throws Exception 抛出异常
     */
    List<T> mgetById(M[] ids, Class<T> clazz) throws Exception;

    /**
     * id数据是否存在
     *
     * @param id    id
     * @param clazz clazz
     * @return {@link T}
     * @throws Exception 抛出异常
     */
    boolean exists(M id, Class<T> clazz) throws Exception;

    /**
     * 普通聚合查询
     * //TODO 添加跨索引
     * 以bucket分组以aggstypes的方式metric度量
     *
     * @param bucketName   bucketName
     * @param metricName   metricName
     * @param aggsType     aggsType
     * @param clazz        clazz
     * @param queryBuilder 查询条件
     * @return {@link Map}
     * @throws Exception 抛出异常
     */
    <K, V> Map<K, V> aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception;


    /**
     * 以aggstypes的方式metric度量
     * //TODO 添加跨索引
     *
     * @param metricName   metricName
     * @param aggsType     aggsType
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @return {@link double}
     * @throws Exception 抛出异常
     */
    double aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;


    /**
     * 下钻聚合查询(无排序默认策略)
     * //TODO 添加跨索引
     * 以bucket分组以aggstypes的方式metric度量
     *
     * @param metricName   metricName
     * @param aggsType     aggsType
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @param bucketNames  bucketNames
     * @return {@link List<Down>}
     * @throws Exception 抛出异常
     */
    List<Down> aggswith2level(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String... bucketNames) throws Exception;


    /**
     * 统计聚合metric度量
     * //TODO 添加跨索引
     *
     * @param metricName   metricName
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @return {@link List<Stats>}
     * @throws Exception 抛出异常
     */
    Stats statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 以bucket分组，统计聚合metric度量
     * //TODO 添加跨索引
     *
     * @param bucketName   bucketName
     * @param metricName   metricName
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @return {@link List<Map<String,Stats>>}
     * @throws Exception 抛出异常
     */
    Map<String, Stats> statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception;


    /**
     * 通用（定制）聚合基础方法
     * //TODO 添加跨索引
     *
     * @param aggregationBuilder 查询条件
     * @param queryBuilder       查询条件
     * @param clazz              clazz
     * @return {@link List<Map<String,Stats>>}
     * @throws Exception 抛出异常
     */
    Aggregations aggs(AggregationBuilder aggregationBuilder, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;


    /**
     * 基数查询
     * //TODO 添加跨索引
     *
     * @param metricName   metricName
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @return {@link long}
     * @throws Exception 抛出异常
     */
    long cardinality(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 百分比聚合 默认聚合见Constant.DEFAULT_PERCSEGMENT
     * //TODO 添加跨索引
     *
     * @param metricName   metricName
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @return {@link Map}
     * @throws Exception 抛出异常
     */
    Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 以百分比聚合
     * //TODO 添加跨索引
     *
     * @param metricName    metricName
     * @param queryBuilder  查询条件
     * @param clazz         clazz
     * @param customSegment customSegment
     * @return {@link Map}
     * @throws Exception 抛出异常
     */
    Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double... customSegment) throws Exception;


    /**
     * 以百分等级聚合 (统计在多少数值之内占比多少)
     * //TODO 添加跨索引
     *
     * @param metricName    metricName
     * @param queryBuilder  查询条件
     * @param clazz         clazz
     * @param customSegment customSegment
     * @return {@link Map}
     * @throws Exception 抛出异常
     */
    Map percentileRanksAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double... customSegment) throws Exception;


    /**
     * 过滤器聚合
     * //TODO 添加跨索引
     * new FiltersAggregator.KeyedFilter("men", QueryBuilders.termQuery("gender", "male"))
     *
     * @param metricName   metricName
     * @param aggsType     aggsType
     * @param clazz        clazz
     * @param queryBuilder 查询条件
     * @param filters      filters
     * @return {@link Map}
     * @throws Exception 抛出异常
     */
    Map filterAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, FiltersAggregator.KeyedFilter... filters) throws Exception;

    /**
     * 直方图聚合
     * //TODO 添加跨索引
     *
     * @param metricName   metricName
     * @param aggsType     aggsType
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @param bucketName   bucketName
     * @param interval     interval
     * @return {@link Map}
     * @throws Exception 抛出异常
     */
    Map histogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, double interval) throws Exception;


    /**
     * 日期直方图聚合
     * //TODO 添加跨索引
     *
     * @param metricName   metricName
     * @param aggsType     aggsType
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @param bucketName   bucketName
     * @param interval     interval
     * @return {@link Map}
     * @throws Exception 抛出异常
     */
    Map dateHistogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, DateHistogramInterval interval) throws Exception;

    /**
     * 批量修改根据id
     *
     * @param list 修改数据集合
     * @return {@link BulkResponse}
     * @throws Exception 抛出异常
     */
    BulkResponse updateRequest(List<T> list) throws Exception;

    /**
     * 指定字段查询返回指定字段数组的数据
     *
     * @param field       字段
     * @param returnField 返回字段数组
     */
    void fieldQuery(String field, String... returnField);

    /**
     * 查询
     *
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @param indexs       索引数组
     * @param size         条数
     * @throws Exception 抛出异常
     * @author: wxf
     * @date: 2020/4/11 9:43
     * @return: {@link List<T>}
     **/
    List<T> search(QueryBuilder queryBuilder, Class<T> clazz, int size, String... indexs) throws Exception;

    /**
     * 方法描述 : 经纬度查询
     * 1、排序方式：首先按查询条件中的排序方式排序，再按经纬度排序
     *
     * @param dto 查询条件参数
     * @throws IOException 抛出异常
     * @author: wxf
     * @date: 2020/4/21 11:15
     * @return: {@link List<T>}
     **/
    PageData<T> locationSearch(EsGeoSearchDTO<T> dto) throws IOException;

    /**
     * 方法描述 : 经纬度查询
     * 1、排序方式：首先按经纬度排序、再按查询条件中的其他排序方式排序
     *
     * @param dto 查询条件参数
     * @throws IOException 抛出异常
     * @author: meitao
     * @date: 2020/4/21 11:15
     * @return: {@link List<T>}
     **/
    PageData<T> locationSearch33(EsGeoSearchDTO<T> dto) throws IOException;

    /**
     * 商户商品搜索
     *
     * @param queryBuilder 查询条件
     * @param clazz        返回的类型
     * @param sort         排序
     * @param indexs       索引
     * @throws Exception 抛出异常
     * @return: {@link List<T>}
     */
    List<T> merchantGoodsSearch(QueryBuilder queryBuilder, Class<T> clazz, Sort sort, String... indexs) throws Exception;

    /**
     * 描述: 嵌套聚合查询 聚合skuList等嵌套字段
     *
     * @param bucketName   分组字段 shopId
     * @param path         skuList
     * @param metricName   skuList的字段
     * @param queryBuilder
     * @param clazz
     * @return java.util.Map<java.lang.String org.elasticsearch.search.aggregations.metrics.stats.Stats>
     * @author zengzhangni
     * @date 2020/9/8 17:23
     * @since v1.4.0
     */
    Map<String, Stats> nestedStatsAggs(String bucketName, String path, String metricName, QueryBuilder queryBuilder, Class clazz) throws Exception;


    /**
     * 描述:
     *
     * @param bucketName
     * @param path
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @return java.util.Map
     * @author zengzhangni
     * @date 2020/9/9 9:35
     * @since v1.4.0
     */
    <K, V> Map<K, V> nestedAggs(String bucketName, String path, String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class clazz) throws Exception;

    /**
     * 描述:嵌套聚合查询
     *
     * @param path
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return org.elasticsearch.search.aggregations.metrics.stats.Stats
     * @author zengzhangni
     * @date 2020/9/8 17:24
     * @since v1.4.0
     */
    Stats nestedStatsAggs(String path, String metricName, QueryBuilder queryBuilder, Class clazz) throws Exception;

    /**
     * 描述:嵌套聚合查询
     *
     * @param path
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @return double
     * @author zengzhangni
     * @date 2020/9/8 17:24
     * @since v1.4.0
     */
    Double nestedAggs(String path, String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class clazz) throws Exception;
}
