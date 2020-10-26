package com.meiyuan.catering.es.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.dto.geo.EsGeoSearchDTO;
import com.meiyuan.catering.es.enums.AggsType;
import com.meiyuan.catering.es.enums.DataType;
import com.meiyuan.catering.es.util.*;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedHistogram;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.min.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.percentiles.*;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


/**
 * @program: esdemo
 * @description: Elasticsearch基础功能组件实现类
 * @author: X-Pacific zhang
 * @create: 2019-01-18 16:04
 **/
@Component
public class ElasticsearchTemplateImpl<T, M> implements ElasticsearchTemplate<T, M> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestHighLevelClient client;

    @Override
    public Response request(Request request) throws Exception {
        Response response = client.getLowLevelClient().performRequest(request);
        return response;
    }

    @Override
    public boolean save(T t) throws Exception {
        MetaData metaData = IndexTools.getIndexType(t.getClass());
        String indexname = metaData.getIndexname();
        String indextype = metaData.getIndextype();
        String id = Tools.getEsId(t);
        IndexRequest indexRequest = null;
        if (StringUtils.isEmpty(id)) {
            indexRequest = new IndexRequest(indexname, indextype);
        } else {
            indexRequest = new IndexRequest(indexname, indextype, id);
        }
        String source = JsonUtils.obj2String(t);
        indexRequest.source(source, XContentType.JSON);
        IndexResponse indexResponse = null;
        indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            logger.info("INDEX CREATE SUCCESS");
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            logger.info("INDEX UPDATE SUCCESS");
        } else {
            return false;
        }
        return true;
    }

    @Override
    public BulkResponse save(List<T> list) throws Exception {
        if (list == null || list.size() == 0) {
            return null;
        }
        T t = list.get(0);
        MetaData metaData = IndexTools.getIndexType(t.getClass());
        String indexname = metaData.getIndexname();
        String indextype = metaData.getIndextype();
        BulkRequest rrr = new BulkRequest();
        for (int i = 0; i < list.size(); i++) {
            T tt = list.get(i);
            String id = Tools.getEsId(tt);
            String source = JsonUtils.obj2String(tt);
            rrr.add(new IndexRequest(indexname, indextype, id)
                    .source(source, XContentType.JSON));
        }
        BulkResponse bulkResponse = client.bulk(rrr, RequestOptions.DEFAULT);
        return bulkResponse;
    }

    @Override
    public BulkResponse updateRequest(List<T> list) throws Exception {
        if (list == null || list.size() == 0) {
            return null;
        }
        T t = list.get(0);
        MetaData metaData = IndexTools.getIndexType(t.getClass());
        String indexname = metaData.getIndexname();
        String indextype = metaData.getIndextype();
        BulkRequest rrr = new BulkRequest();
        for (int i = 0; i < list.size(); i++) {
            T tt = list.get(i);
            String id = Tools.getEsId(tt);
            if (StringUtils.isEmpty(id)) {
                throw new Exception("索引ID为空");
            }
            // ES是根据JSON字段更新 JSON里面有那些字段 ES就更新那些字段 字段值是null也会更新会null
            String jsonStr = JSON.toJSONString(tt, SerializerFeature.WriteMapNullValue);

            rrr.add(new UpdateRequest(indexname, indextype, id)
                    .doc(jsonStr, XContentType.JSON));
        }
        BulkResponse bulkResponse = client.bulk(rrr, RequestOptions.DEFAULT);
        return bulkResponse;
    }

    @Override
    public boolean update(T t) throws Exception {
        MetaData metaData = IndexTools.getIndexType(t.getClass());
        String indexname = metaData.getIndexname();
        String indextype = metaData.getIndextype();
        String id = Tools.getEsId(t);
        if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
        UpdateRequest updateRequest = new UpdateRequest(indexname, indextype, id);
        String jsonStr = JSONObject.toJSON(Tools.getFieldValue(t)).toString();
        updateRequest.doc(jsonStr, XContentType.JSON);
        UpdateResponse updateResponse = null;
        updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
            logger.info("INDEX CREATE SUCCESS");
        } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            logger.info("INDEX UPDATE SUCCESS");
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateCover(T t) throws Exception {
        return save(t);
    }

    @Override
    public boolean delete(T t) throws Exception {
        MetaData metaData = IndexTools.getIndexType(t.getClass());
        String indexname = metaData.getIndexname();
        String indextype = metaData.getIndextype();
        String id = Tools.getEsId(t);
        if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
        DeleteRequest deleteRequest = new DeleteRequest(indexname, indextype, id);
        DeleteResponse deleteResponse = null;
        deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
            logger.info("INDEX DELETE SUCCESS");
        } else {
            return false;
        }
        return true;
    }

    @Override
    public BulkByScrollResponse deleteByCondition(QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        DeleteByQueryRequest request = new DeleteByQueryRequest(indexname);
        request.setQuery(queryBuilder);
        return client.deleteByQuery(request, RequestOptions.DEFAULT);
    }


    @Override
    public SearchResponse search(SearchRequest searchRequest) throws IOException {
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse;
    }

    @Override
    public List<T> search(QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        return search(queryBuilder, clazz, indexname);
    }

    @Override
    public List<T> search(QueryBuilder queryBuilder, Class<T> clazz, String... indexs) throws Exception {
        List<T> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(indexs);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(Constant.DEFALT_PAGE_SIZE);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
            list.add(t);
        }
        return list;
    }

    @Override
    public List<T> search(QueryBuilder queryBuilder, Class<T> clazz, Integer pageNum, Integer pageSize, String... indexs) throws Exception {
        List<T> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(indexs);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from((pageNum - 1) * pageSize);
        searchSourceBuilder.size(pageSize);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
            list.add(t);
        }
        return list;
    }

    @Override
    public List<T> search(QueryBuilder queryBuilder, Class<T> clazz, Attach attach) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        List<T> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(indexname);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设定返回source
        if (attach != null && (attach.getExcludes() != null || attach.getIncludes() != null)) {
            searchSourceBuilder.fetchSource(attach.getIncludes(), attach.getExcludes());
        }
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(Constant.DEFALT_PAGE_SIZE);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
            list.add(t);
        }
        return list;
    }

    @Override
    public long count(QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        return count(queryBuilder, clazz, indexname);
    }

    @Override
    public long count(QueryBuilder queryBuilder, Class<T> clazz, String... indexs) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        CountRequest countRequest = new CountRequest(indexs);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        countRequest.source(searchSourceBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        long count = countResponse.getCount();
        return count;
    }

    @Override
    public T getById(M id, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexname();
        String indextype = metaData.getIndextype();
        if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
        GetRequest getRequest = new GetRequest(indexname, indextype, id.toString());
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            return JsonUtils.string2Obj(getResponse.getSourceAsString(), clazz);
        }
        return null;
    }

    @Override
    public List<T> mgetById(M[] ids, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexname();
        String indextype = metaData.getIndextype();
        MultiGetRequest request = new MultiGetRequest();
        for (int i = 0; i < ids.length; i++) {
            request.add(new MultiGetRequest.Item(indexname, indextype, ids[i].toString()));
        }
        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
        List<T> list = new ArrayList<>();
        for (int i = 0; i < response.getResponses().length; i++) {
            MultiGetItemResponse item = response.getResponses()[i];
            GetResponse getResponse = item.getResponse();
            if (getResponse.isExists()) {
                list.add(JsonUtils.string2Obj(getResponse.getSourceAsString(), clazz));
            }
        }
        return list;
    }

    @Override
    public boolean exists(M id, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexname();
        String indextype = metaData.getIndextype();
        if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
        GetRequest getRequest = new GetRequest(indexname, indextype, id.toString());
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            return true;
        }
        return false;
    }


    @Override
    public <K, V> Map<K, V> aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        Field fBucket = clazz.getDeclaredField(bucketName.replaceAll(KEYWORD, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        if (fBucket == null) {
            throw new Exception("bucket field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        bucketName = genKeyword(fBucket, bucketName);

        String by = "by_" + bucketName;
        String me = aggsType.toString() + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregation = AggregationBuilders.terms(by)
                .field(bucketName);
        //默认按照聚合结果降序排序
        aggregation.order(BucketOrder.aggregation(me, false));
        if (AggsType.count == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation.size(Constant.AGG_RESULT_COUNT));


        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        if (metaData.isPrintLog()) {
            logger.info(searchSourceBuilder.toString());
        }
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms byRiskCode = aggregations.get(by);
        Map map = new LinkedHashMap();
        for (Terms.Bucket bucket : byRiskCode.getBuckets()) {
            if (AggsType.count == aggsType) {
                ValueCount count = bucket.getAggregations().get(me);
                long value = count.getValue();
                map.put(bucket.getKey(), Double.parseDouble(String.valueOf(value)));
            } else if (AggsType.min == aggsType) {
                ParsedMin min = bucket.getAggregations().get(me);
                double value = min.getValue();
                map.put(bucket.getKey(), value);
            } else if (AggsType.max == aggsType) {
                ParsedMax max = bucket.getAggregations().get(me);
                double value = max.getValue();
                map.put(bucket.getKey(), value);
            } else if (AggsType.sum == aggsType) {
                ParsedSum sum = bucket.getAggregations().get(me);
                double value = sum.getValue();
                map.put(bucket.getKey(), value);
            } else if (AggsType.avg == aggsType) {
                ParsedAvg avg = bucket.getAggregations().get(me);
                double value = avg.getValue();
                map.put(bucket.getKey(), value);
            }
        }
        return (Map<K, V>) map;
    }

    @Override
    public List<Down> aggswith2level(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String... bucketNames) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (bucketNames == null) {
            throw new NullPointerException();
        }
        int bucketNamesLength = 2;
        if (bucketNames.length != bucketNamesLength) {
            throw new Exception("仅支持两层下钻聚合!");
        }
        Field[] fBuckets = new Field[bucketNames.length];
        for (int i = 0; i < bucketNames.length; i++) {
            fBuckets[i] = clazz.getDeclaredField(bucketNames[i].replaceAll(KEYWORD, ""));
            if (fBuckets[i] == null) {
                throw new Exception("bucket field is null");
            }
        }
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        String me = aggsType.toString() + "_" + metricName;

        String[] bys = new String[bucketNames.length];
        for (int i = 0; i < fBuckets.length; i++) {
            bucketNames[i] = genKeyword(fBuckets[i], bucketNames[i]);
            bys[i] = "by_" + bucketNames[i];
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder[] termsAggregationBuilders = new TermsAggregationBuilder[bucketNames.length];
        for (int i = 0; i < bucketNames.length; i++) {
            TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(bys[i]).field(bucketNames[i]);
            termsAggregationBuilders[i] = aggregationBuilder;
        }
        for (int i = 0; i < termsAggregationBuilders.length; i++) {
            if (i != termsAggregationBuilders.length - 1) {
                termsAggregationBuilders[i].subAggregation(termsAggregationBuilders[i + 1]);
            }
        }
        termsAggregationBuilders(metricName, aggsType, me, termsAggregationBuilders);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(termsAggregationBuilders[0]);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        //下面不支持2层以上的下钻
        List<Down> downList = new ArrayList<>();
        Terms terms1 = searchResponse.getAggregations().get(bys[0]);
        Terms terms2;
        for (Terms.Bucket bucket : terms1.getBuckets()) {
            terms2 = bucket.getAggregations().get(bys[1]);
            for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                Down down = new Down();
                down.setLevel1Key(bucket.getKey().toString());
                down.setLevel2Key(bucket2.getKey().toString());
                downValue(aggsType, me, bucket2, down);
                downList.add(down);
            }
        }
        return downList;
    }

    private void downValue(AggsType aggsType, String me, Terms.Bucket bucket2, Down down) {
        if (AggsType.count == aggsType) {
            ValueCount count = bucket2.getAggregations().get(me);
            long value = count.getValue();
            down.setValue(value);
        } else if (AggsType.min == aggsType) {
            ParsedMin min = bucket2.getAggregations().get(me);
            double value = min.getValue();
            down.setValue(value);
        } else if (AggsType.max == aggsType) {
            ParsedMax max = bucket2.getAggregations().get(me);
            double value = max.getValue();
            down.setValue(value);
        } else if (AggsType.sum == aggsType) {
            ParsedSum sum = bucket2.getAggregations().get(me);
            double value = sum.getValue();
            down.setValue(value);
        } else if (AggsType.avg == aggsType) {
            ParsedAvg avg = bucket2.getAggregations().get(me);
            double value = avg.getValue();
            down.setValue(value);
        }
    }

    private void termsAggregationBuilders(String metricName, AggsType aggsType, String me, TermsAggregationBuilder[] termsAggregationBuilders) {
        if (AggsType.count == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
    }


    private static final String KEYWORD = ".keyword";

    /**
     * 组织字段是否带有.KEYWORD
     *
     * @param field
     * @param name
     * @return
     */
    private String genKeyword(Field field, String name) {
        ESMapping esMapping = field.getAnnotation(ESMapping.class);
        //带着.keyword直接忽略
        if (name == null || name.indexOf(KEYWORD) > -1) {
            return name;
        }
        //只要keyword是true就要拼接
        //没配注解，但是类型是字符串，默认keyword是true
        if (esMapping == null) {
            if (field.getType() == String.class) {
                return name + KEYWORD;
            }
        }
        //配了注解，但是类型是字符串，默认keyword是true
        else {
            if (esMapping.datatype() == DataType.text_type && esMapping.keyword() == true) {
                return name + KEYWORD;
            }
        }
        return name;
    }

    @Override
    public double aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        String me = aggsType.toString() + "_" + metricName;
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        if (AggsType.count == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.avg(me).field(metricName));
        }
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        if (AggsType.count == aggsType) {
            ValueCount count = searchResponse.getAggregations().get(me);
            long value = count.getValue();
            return Double.parseDouble(String.valueOf(value));
        } else if (AggsType.min == aggsType) {
            ParsedMin min = searchResponse.getAggregations().get(me);
            double value = min.getValue();
            return value;
        } else if (AggsType.max == aggsType) {
            ParsedMax max = searchResponse.getAggregations().get(me);
            double value = max.getValue();
            return value;
        } else if (AggsType.sum == aggsType) {
            ParsedSum sum = searchResponse.getAggregations().get(me);
            double value = sum.getValue();
            return value;
        } else if (AggsType.avg == aggsType) {
            ParsedAvg avg = searchResponse.getAggregations().get(me);
            double value = avg.getValue();
            return value;
        }
        return 0d;
    }

    @Override
    public Stats statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        String me = "stats";
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        StatsAggregationBuilder aggregation = AggregationBuilders.stats(me).field(metricName);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Stats stats = searchResponse.getAggregations().get(me);
        return stats;
    }

    @Override
    public Map<String, Stats> statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        Field fBucket = clazz.getDeclaredField(bucketName.replaceAll(KEYWORD, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        if (fBucket == null) {
            throw new Exception("bucket field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        bucketName = genKeyword(fBucket, bucketName);

        String by = "by_" + bucketName;
        String me = "stats" + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregation = AggregationBuilders.terms(by)
                .field(bucketName);
        //默认按照count的降序排序
        aggregation.order(BucketOrder.count(false));
        aggregation.subAggregation(AggregationBuilders.stats(me).field(metricName)).size(Constant.AGG_RESULT_COUNT);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms byRiskCode = aggregations.get(by);
        Map<String, Stats> map = new LinkedHashMap<>();
        for (Terms.Bucket bucket : byRiskCode.getBuckets()) {
            Stats stats = bucket.getAggregations().get(me);
            map.put(bucket.getKey().toString(), stats);
        }
        return map;
    }

    @Override
    public Aggregations aggs(AggregationBuilder aggregationBuilder, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregationBuilder);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        if (metaData.isPrintLog()) {
            logger.info(searchSourceBuilder.toString());
        }
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse.getAggregations();
    }

    @Override
    public long cardinality(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        String me = "cardinality_" + metricName;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        CardinalityAggregationBuilder aggregation = AggregationBuilders
                .cardinality(me)
                .field(metricName);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Cardinality agg = searchResponse.getAggregations().get(me);
        return agg.getValue();
    }

    @Override
    public Map<Double, Double> percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        return percentilesAggs(metricName, queryBuilder, clazz, Constant.DEFAULT_PERCSEGMENT);
    }

    @Override
    public Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double... customSegment) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        String me = "percentiles_" + metricName;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        PercentilesAggregationBuilder aggregation = AggregationBuilders.percentiles(me).field(metricName).percentiles(customSegment);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Map<Double, Double> map = new LinkedHashMap<>();
        Percentiles agg = searchResponse.getAggregations().get(me);
        for (Percentile entry : agg) {
            double percent = entry.getPercent();
            double value = entry.getValue();
            map.put(percent, value);
        }
        return map;
    }

    @Override
    public Map percentileRanksAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double... customSegment) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        String me = "percentiles_" + metricName;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        PercentileRanksAggregationBuilder aggregation = AggregationBuilders.percentileRanks(me, customSegment).field(metricName);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Map<Double, Double> map = new LinkedHashMap<>();
        PercentileRanks agg = searchResponse.getAggregations().get(me);
        for (Percentile entry : agg) {
            double percent = entry.getPercent();
            double value = entry.getValue();
            map.put(percent, value);
        }
        return map;
    }


    @Override
    public Map filterAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, FiltersAggregator.KeyedFilter... filters) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (filters == null) {
            throw new NullPointerException();
        }
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        String me = aggsType.toString() + "_" + metricName;
        AggregationBuilder aggregation = AggregationBuilders.filters("filteragg", filters);
        searchSourceBuilder.size(0);
        if (AggsType.count == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Filters agg = searchResponse.getAggregations().get("filteragg");
        Map map = new LinkedHashMap();
        for (Filters.Bucket entry : agg.getBuckets()) {
            if (AggsType.count == aggsType) {
                ValueCount count = entry.getAggregations().get(me);
                long value = count.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.min == aggsType) {
                ParsedMin min = entry.getAggregations().get(me);
                double value = min.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.max == aggsType) {
                ParsedMax max = entry.getAggregations().get(me);
                double value = max.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.sum == aggsType) {
                ParsedSum sum = entry.getAggregations().get(me);
                double value = sum.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.avg == aggsType) {
                ParsedAvg avg = entry.getAggregations().get(me);
                double value = avg.getValue();
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    @Override
    public Map histogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, double interval) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        Field fBucket = clazz.getDeclaredField(bucketName.replaceAll(KEYWORD, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        if (fBucket == null) {
            throw new Exception("bucket field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        bucketName = genKeyword(fBucket, bucketName);
        String by = "by_" + bucketName;
        String me = aggsType.toString() + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        AggregationBuilder aggregation = AggregationBuilders.histogram(by).field(bucketName).interval(interval);
        searchSourceBuilder.size(0);
        if (AggsType.count == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        ParsedHistogram agg = searchResponse.getAggregations().get(by);
        Map map = new LinkedHashMap();
        for (Histogram.Bucket entry : agg.getBuckets()) {
            if (AggsType.count == aggsType) {
                ValueCount count = entry.getAggregations().get(me);
                long value = count.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.min == aggsType) {
                ParsedMin min = entry.getAggregations().get(me);
                double value = min.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.max == aggsType) {
                ParsedMax max = entry.getAggregations().get(me);
                double value = max.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.sum == aggsType) {
                ParsedSum sum = entry.getAggregations().get(me);
                double value = sum.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.avg == aggsType) {
                ParsedAvg avg = entry.getAggregations().get(me);
                double value = avg.getValue();
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    @Override
    public Map dateHistogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, DateHistogramInterval interval) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        Field fBucket = clazz.getDeclaredField(bucketName.replaceAll(KEYWORD, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        if (fBucket == null) {
            throw new Exception("bucket field is null");
        } else if (fBucket.getType() != Date.class) {
            throw new Exception("bucket type is not support");
        }
        ESMapping esMapping = fBucket.getAnnotation(ESMapping.class);
        if (esMapping != null && esMapping.datatype() != DataType.date_type) {
            throw new Exception("bucket type is not support");
        }
        metricName = genKeyword(fMetric, metricName);
        bucketName = genKeyword(fBucket, bucketName);
        String by = "by_" + bucketName;
        String me = aggsType.toString() + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        AggregationBuilder aggregation = AggregationBuilders.dateHistogram(by).field(bucketName).dateHistogramInterval(interval);
        searchSourceBuilder.size(0);
        if (AggsType.count == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        ParsedDateHistogram agg = searchResponse.getAggregations().get(by);
        Map map = new LinkedHashMap();
        for (Histogram.Bucket entry : agg.getBuckets()) {
            if (AggsType.count == aggsType) {
                ValueCount count = entry.getAggregations().get(me);
                long value = count.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.min == aggsType) {
                ParsedMin min = entry.getAggregations().get(me);
                double value = min.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.max == aggsType) {
                ParsedMax max = entry.getAggregations().get(me);
                double value = max.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.sum == aggsType) {
                ParsedSum sum = entry.getAggregations().get(me);
                double value = sum.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.avg == aggsType) {
                ParsedAvg avg = entry.getAggregations().get(me);
                double value = avg.getValue();
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    @Override
    public boolean deleteById(M id, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexname();
        String indextype = metaData.getIndextype();
        if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
        DeleteRequest deleteRequest = new DeleteRequest(indexname, indextype, id.toString());
        DeleteResponse deleteResponse = null;
        deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
            logger.info("INDEX DELETE SUCCESS");
        } else {
            return false;
        }
        return true;
    }

    @Override
    public PageData<T> search(QueryBuilder queryBuilder, PageSortHighLight pageSortHighLight, Class<T> clazz, String index) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData == null ? new String[]{index} : metaData.getSearchIndexNames();
        PageData<T> pageData = new PageData<>();
        List<T> list = new ArrayList<>();
        if (pageSortHighLight == null) {
            throw new NullPointerException("PageSortHighLight不能为空!");
        }
        pageData = search(queryBuilder, pageSortHighLight, clazz, indexname);
        return pageData;
    }

    @Override
    public PageData<T> search(QueryBuilder queryBuilder, PageSortHighLight pageSortHighLight, Class<T> clazz, String... indexs) throws Exception {
        List<T> list = new ArrayList<>();
        if (pageSortHighLight == null) {
            throw new NullPointerException("PageSortHighLight不能为空!");
        }
        SearchRequest searchRequest = new SearchRequest(indexs);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        //分页
        searchSourceBuilder.from((pageSortHighLight.getCurrentPage() - 1) * pageSortHighLight.getPageSize());
        searchSourceBuilder.size(pageSortHighLight.getPageSize());
        if (null != pageSortHighLight.getSort()) {
            //排序
            Sort sort = pageSortHighLight.getSort();
            List<Sort.Order> orders = sort.listOrders();
            orders.forEach(order ->
                    searchSourceBuilder.sort(new FieldSortBuilder(order.getProperty()).order(order.getDirection()))
            );
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
            //替换高亮字段
            Map<String, HighlightField> hmap = hit.getHighlightFields();
            hmap.forEach((k, v) ->
                    {
                        try {
                            Object obj = BeanTools.mapToObject(hmap, clazz);
                            BeanUtils.copyProperties(obj, t, BeanTools.getNoValuePropertyNames(obj));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
            list.add(t);
        }
        PageData<T> pageData = new PageData<>();
        pageData.setTotal((int) hits.getTotalHits());
        pageData.setList(list);
        return pageData;
    }


    @Override
    public List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz, long time) throws Exception {
        if (queryBuilder == null) {
            throw new NullPointerException();
        }
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        List<T> list = new ArrayList<>();
        Scroll scroll = new Scroll(TimeValue.timeValueHours(time));
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(Constant.DEFAULT_SCROLL_PERPAGE);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        //第一把查询出的结果
        for (SearchHit hit : searchHits) {
            T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
            list.add(t);
        }
        while (searchHits != null && searchHits.length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
                list.add(t);
            }
        }
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse
                = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        boolean succeeded = clearScrollResponse.isSucceeded();
        return list;
    }

    @Override
    public List<T> searchTemplate(Map<String, Object> templateParams, String templateName, Class<T> clazz) throws Exception {
        if (true) {
            throw new Exception("nonsupport!");
        }
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        SearchResponse sr = new SearchTemplateRequestBuilder(null)
                .setScript("templateName")
                .setScriptType(ScriptType.STORED)
                .setScriptParams(templateParams)
                .setRequest(new SearchRequest(indexname))
                .get()
                .getResponse();
        return null;
    }

    @Override
    public List<T> searchTemplateBySource(Map<String, Object> templateParams, String templateSource, Class<T> clazz) throws Exception {
        if (true) {
            throw new Exception("nonsupport!");
        }
        return null;
    }

    @Override
    public List<T> saveTemplate(String templateName, String templateSource, Class<T> clazz) throws Exception {
        if (true) {
            throw new Exception("nonsupport!");
        }
        return null;
    }

    @Override
    public List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        return scroll(queryBuilder, clazz, Constant.DEFAULT_SCROLL_TIME);
    }


    @Override
    public List<String> completionSuggest(String fieldName, String fieldValue, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        String indextype = metaData.getIndextype();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SuggestBuilder suggestBuilder = new SuggestBuilder();

        CompletionSuggestionBuilder completionSuggestionBuilder = new
                CompletionSuggestionBuilder(fieldName + ".suggest");
        completionSuggestionBuilder.text(fieldValue);
        completionSuggestionBuilder.size(Constant.COMPLETION_SUGGESTION_SIZE);
        suggestBuilder.addSuggestion("suggest_" + fieldName, completionSuggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);

        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Suggest suggest = searchResponse.getSuggest();
        if (suggest == null) {
            return null;
        }
        CompletionSuggestion completionSuggestion = suggest.getSuggestion("suggest_" + fieldName);
        List<String> list = new ArrayList<>();
        for (CompletionSuggestion.Entry entry : completionSuggestion.getEntries()) {
            for (CompletionSuggestion.Entry.Option option : entry) {
                String suggestText = option.getText().string();
                list.add(suggestText);
            }
        }
        return list;
    }

    private int getTotalPages(long totalHits, int pageSize) {
        return pageSize == 0 ? 1 : (int) Math.ceil((double) totalHits / (double) pageSize);
    }

    private String nestedField(String path, String metricName) {
        return path + "." + metricName;
    }

    @Override
    public void fieldQuery(String field, String... returnField) {

    }


    @Override
    public List<T> search(QueryBuilder queryBuilder, Class<T> clazz, int size, String... indexs) throws Exception {
        List<T> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(indexs);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(size);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
            list.add(t);
        }
        return list;
    }

    @Override
    public PageData<T> locationSearch(EsGeoSearchDTO<T> dto) throws IOException {
        // 经纬度筛选
        GeoDistanceQueryBuilder geoQueryBuilder = QueryBuilders.geoDistanceQuery(dto.getLocation())
                // 维度，经度
                .point(dto.getLat(), dto.getLon())
                .distance(dto.getDistance(), dto.getDistanceUnit())
                .geoDistance(GeoDistance.ARC);
        //按距离排序
        GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(dto.getLocation(), dto.getLat(), dto.getLon())
                .unit(dto.getDistanceUnit())
                .order(dto.getSortOrder());
        int pageSize = dto.getPageSize().intValue();
        BoolQueryBuilder queryBuilder = dto.getQueryBuilder();
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .from((dto.getPageNo().intValue() - 1) * pageSize)
                .size(pageSize)
                .query(queryBuilder);
        if (null != dto.getSortOrder()) {
            Sort sortOrder = dto.getSort();
            if (null != sortOrder) {
                List<Sort.Order> orders = sortOrder.listOrders();
                orders.forEach(order -> {
                            if (BaseUtil.judgeString(order.getProperty())) {
                                searchSourceBuilder.sort(new FieldSortBuilder(order.getProperty()).order(order.getDirection()));
                            }
                        }
                );
            }
        }
        searchSourceBuilder.sort(sort);
        // 查询请求
        SearchRequest searchRequest = new SearchRequest(dto.getIndex());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        List<T> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            T t = JsonUtils.string2Obj(hit.getSourceAsString(), dto.getClazz());
            list.add(t);
        }
        PageData<T> pageData = new PageData<>();
        pageData.setTotal((int) hits.getTotalHits());
        pageData.setLastPage(dto.getPageSize(), dto.getPageNo());
        pageData.setList(list);
        return pageData;
    }

    @Override
    public PageData<T> locationSearch33(EsGeoSearchDTO<T> dto) throws IOException {
        //1、查询条件组装
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();

        //1.1设置按距离排序条件【按排序条件先后设置排序条件】
        GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(dto.getLocation(), dto.getLat(), dto.getLon())
                .unit(dto.getDistanceUnit())
                .order(dto.getSortOrder());
        searchSourceBuilder.sort(sort);

        if (null != dto.getSortOrder()) {
            Sort sortOrder = dto.getSort();
            if (null != sortOrder) {
                List<Sort.Order> orders = sortOrder.listOrders();
                orders.forEach(order -> {
                            if (BaseUtil.judgeString(order.getProperty())) {
                                searchSourceBuilder.sort(new FieldSortBuilder(order.getProperty()).order(order.getDirection()));
                            }
                        }
                );
            }
        }

        //1.3设置分页条件
        int pageSize = dto.getPageSize().intValue();
        searchSourceBuilder.from((dto.getPageNo().intValue() - 1) * pageSize);
        searchSourceBuilder.size(pageSize);
        //1.2设置筛选条件
        searchSourceBuilder.query(dto.getQueryBuilder());

        //2、查询请求组装
        SearchRequest searchRequest = new SearchRequest(dto.getIndex());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        List<T> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            T t = JsonUtils.string2Obj(hit.getSourceAsString(), dto.getClazz());
            list.add(t);
        }
        PageData<T> pageData = new PageData<>();
        pageData.setTotal((int) hits.getTotalHits());
        pageData.setLastPage(dto.getPageSize(), dto.getPageNo());
        pageData.setList(list);
        return pageData;
    }

    @Override
    public List<T> merchantGoodsSearch(QueryBuilder queryBuilder, Class<T> clazz, Sort sort, String... indexs) throws Exception {
        List<T> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(indexs);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(Constant.DEFALT_PAGE_SIZE);
        if (BaseUtil.judgeList(sort.listOrders())) {
            List<Sort.Order> orders = sort.listOrders();
            orders.forEach(order -> searchSourceBuilder.sort(new FieldSortBuilder(order.getProperty()).order(order.getDirection())));
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
            list.add(t);
        }
        return list;
    }

    @Override
    public Map<String, Stats> nestedStatsAggs(String bucketName, String path, String metricName, QueryBuilder queryBuilder, Class clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();

        String by = "by_" + bucketName;
        String me = "stats" + "_" + metricName;
        String nd = "nested";


        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        NestedAggregationBuilder nested = AggregationBuilders.nested(nd, path);

        StatsAggregationBuilder statsAggregation = AggregationBuilders.stats(me).field(nestedField(path, metricName));
        nested.subAggregation(statsAggregation);

        TermsAggregationBuilder aggregation = AggregationBuilders.terms(by)
                .field(bucketName).size(Constant.AGG_RESULT_COUNT);
//        //默认按照count的降序排序
        aggregation.order(BucketOrder.count(false));
        aggregation.subAggregation(nested);


        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms terms = aggregations.get(by);
        Map<String, Stats> map = new LinkedHashMap<>();
        for (Terms.Bucket bucket : terms.getBuckets()) {
            ParsedNested parsedNested = bucket.getAggregations().get(nd);
            Stats stats = parsedNested.getAggregations().get(me);
            map.put(bucket.getKey().toString(), stats);
        }
        return map;
    }


    @Override
    public <K, V> Map<K, V> nestedAggs(String bucketName, String path, String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();

        //定义聚合临时变量不需要加keyword
        String by = "by_" + bucketName.replaceAll(KEYWORD, "");
        String me = aggsType.toString() + "_" + metricName.replaceAll(KEYWORD, "");
        String nd = "nested";

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        NestedAggregationBuilder nested = AggregationBuilders.nested(nd, path);

        String field = nestedField(path, metricName);

        if (AggsType.count == aggsType) {
            nested.subAggregation(AggregationBuilders.count(me).field(field));
        } else if (AggsType.min == aggsType) {
            nested.subAggregation(AggregationBuilders.min(me).field(field));
        } else if (AggsType.max == aggsType) {
            nested.subAggregation(AggregationBuilders.max(me).field(field));
        } else if (AggsType.sum == aggsType) {
            nested.subAggregation(AggregationBuilders.sum(me).field(field));
        } else if (AggsType.avg == aggsType) {
            nested.subAggregation(AggregationBuilders.avg(me).field(field));
        }

        TermsAggregationBuilder aggregation = AggregationBuilders.terms(by)
                .field(bucketName).size(Constant.AGG_RESULT_COUNT);
        aggregation.order(BucketOrder.count(false));
        aggregation.subAggregation(nested);

        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);

        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms terms = aggregations.get(by);
        Map map = new LinkedHashMap();
        for (Terms.Bucket bucket : terms.getBuckets()) {
            ParsedNested parsedNested = bucket.getAggregations().get(nd);
            if (AggsType.count == aggsType) {
                ValueCount count = parsedNested.getAggregations().get(me);
                long value = count.getValue();
                map.put(bucket.getKey(), Double.parseDouble(String.valueOf(value)));
            } else if (AggsType.min == aggsType) {
                ParsedMin min = parsedNested.getAggregations().get(me);
                double value = min.getValue();
                map.put(bucket.getKey(), value);
            } else if (AggsType.max == aggsType) {
                ParsedMax max = parsedNested.getAggregations().get(me);
                double value = max.getValue();
                map.put(bucket.getKey(), value);
            } else if (AggsType.sum == aggsType) {
                ParsedSum sum = parsedNested.getAggregations().get(me);
                double value = sum.getValue();
                map.put(bucket.getKey(), value);
            } else if (AggsType.avg == aggsType) {
                ParsedAvg avg = parsedNested.getAggregations().get(me);
                double value = avg.getValue();
                map.put(bucket.getKey(), value);
            }
        }
        return map;
    }

    @Override
    public Stats nestedStatsAggs(String path, String metricName, QueryBuilder queryBuilder, Class clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        String me = "stats";
        String nd = "nested";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);

        //嵌套
        NestedAggregationBuilder nested = AggregationBuilders.nested(nd, path);

        StatsAggregationBuilder aggregation = AggregationBuilders.stats(me).field(nestedField(path, metricName));
        nested.subAggregation(aggregation);

        searchSourceBuilder.aggregation(nested);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        ParsedNested nested1 = aggregations.get(nd);
        Stats stats = nested1.getAggregations().get(me);
        return stats;
    }

    @Override
    public Double nestedAggs(String path, String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String[] indexname = metaData.getSearchIndexNames();
        String me = aggsType.toString() + "_" + metricName;
        String nd = "nested";

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);

        NestedAggregationBuilder nested = AggregationBuilders.nested(nd, path);

        if (AggsType.count == aggsType) {
            nested.subAggregation(AggregationBuilders.count(me).field(nestedField(path, metricName)));
        } else if (AggsType.min == aggsType) {
            nested.subAggregation(AggregationBuilders.min(me).field(nestedField(path, metricName)));
        } else if (AggsType.max == aggsType) {
            nested.subAggregation(AggregationBuilders.max(me).field(nestedField(path, metricName)));
        } else if (AggsType.sum == aggsType) {
            nested.subAggregation(AggregationBuilders.sum(me).field(nestedField(path, metricName)));
        } else if (AggsType.avg == aggsType) {
            nested.subAggregation(AggregationBuilders.avg(me).field(nestedField(path, metricName)));
        }
        searchSourceBuilder.aggregation(nested);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        ParsedNested parsedNested = searchResponse.getAggregations().get(nd);
        Double d = null;
        if (AggsType.count == aggsType) {
            ValueCount count = parsedNested.getAggregations().get(me);
            long value = count.getValue();
            d = Double.parseDouble(String.valueOf(value));
        } else if (AggsType.min == aggsType) {
            ParsedMin min = parsedNested.getAggregations().get(me);
            d = min.getValue();
        } else if (AggsType.max == aggsType) {
            ParsedMax max = parsedNested.getAggregations().get(me);
            d = max.getValue();

        } else if (AggsType.sum == aggsType) {
            ParsedSum sum = parsedNested.getAggregations().get(me);
            d = sum.getValue();
        } else if (AggsType.avg == aggsType) {
            ParsedAvg avg = parsedNested.getAggregations().get(me);
            d = avg.getValue();
        }
        return d != null && Double.isInfinite(d) ? null : d;
    }

}
