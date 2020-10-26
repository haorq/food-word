package com.meiyuan.catering.es.index;

import com.meiyuan.catering.es.util.IndexTools;
import com.meiyuan.catering.es.util.MappingData;
import com.meiyuan.catering.es.util.MetaData;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;

import java.io.IOException;

/**
 * @program: esdemo
 * @description: 索引结构基础方法实现类
 * @author: X-Pacific zhang
 * @create: 2019-01-29 10:05
 **/
@Component
@Slf4j
public class ElasticsearchIndexImpl<T> implements ElasticsearchIndex<T> {
    @Autowired
    RestHighLevelClient client;


    @Override
    public void createIndex(Class<T> clazz) throws Exception{
        MetaData metaData = IndexTools.getMetaData(clazz);
        CreateIndexRequest request = new CreateIndexRequest(metaData.getIndexname());

        StringBuffer source = new StringBuffer();
        source.append("  {\n" +
                "    \""+metaData.getIndextype()+"\": {\n" +
                "      \"properties\": {\n");
        MappingData[] mappingDataList = IndexTools.getMappingData(clazz);

        boolean isAutocomplete = this.appendStringBuffer(source, mappingDataList);

        if(isAutocomplete){
            request.settings(Settings.builder()
                    .put("index.number_of_shards", metaData.getNumberOfShards())
                    .put("index.number_of_replicas", metaData.getNumberOfReplicas())
                    .put("analysis.filter.autocomplete_filter.type","edge_ngram")
                    .put("analysis.filter.autocomplete_filter.min_gram",1)
                    .put("analysis.filter.autocomplete_filter.max_gram",20)
                    .put("analysis.analyzer.autocomplete.type","custom")
                    .put("analysis.analyzer.autocomplete.tokenizer","standard")
                    .putList("analysis.analyzer.autocomplete.filter",new String[]{"lowercase","autocomplete_filter"})
            );
        }else{
            request.settings(Settings.builder()
                    .put("index.number_of_shards", metaData.getNumberOfShards())
                    .put("index.number_of_replicas", metaData.getNumberOfReplicas())
            );
        }
        metaData.getIndextype();
        //类型定义
        request.mapping(
                metaData.getIndextype(),
                //类型映射，需要的是一个JSON字符串
                source.toString(),
                XContentType.JSON);
        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            //返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：
            //指示是否所有节点都已确认请求
            boolean acknowledged = createIndexResponse.isAcknowledged();
           log.info("create indexname:{},acknowledged:{} ",metaData.getIndexname(),acknowledged);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean appendStringBuffer(StringBuffer source,MappingData[] mappingDataList){
        boolean isAutocomplete = false;
        for (int i = 0; i < mappingDataList.length; i++) {
            MappingData mappingData = mappingDataList[i];
            if(mappingData == null || mappingData.getFieldName() == null){
                continue;
            }
            source.append(" \""+mappingData.getFieldName()+"\": {\n");
            source.append(" \"type\": \""+mappingData.getDatatype()+"\"\n");
            if(!StringUtils.isEmpty(mappingData.getCopyTo())){
                source.append(" ,\"copy_to\": \""+mappingData.getCopyTo()+"\"\n");
            }
            if(!mappingData.isAllowSearch()){
                source.append(" ,\"index\": false\n");
            }
            if(mappingData.isAutocomplete() && textOrKeyword(mappingData)){
                source.append(" ,\"analyzer\": \"autocomplete\"\n");
                source.append(" ,\"search_analyzer\": \"standard\"\n");
                isAutocomplete = true;
            }else if("text".equals(mappingData.getDatatype())){
                source.append(" ,\"analyzer\": \"" + mappingData.getAnalyzer() + "\"\n");
                source.append(" ,\"search_analyzer\": \"" + mappingData.getSearchAnalyzer() + "\"\n");
            }
            if(mappingData.isKeyword() && !"keyword".equals(mappingData.getDatatype()) && mappingData.isSuggest()){
                keywordAndIsSuggest(source, mappingData);
            }else if(mappingData.isKeyword() && !"keyword".equals(mappingData.getDatatype())
                    && !mappingData.isSuggest()&& !"nested".equals(mappingData.getDatatype())
                    && !"geo_point".equals(mappingData.getDatatype())){
                keyWord(source, mappingData);
            }else if(!mappingData.isKeyword() && mappingData.isSuggest()){
                noKeyWordAndIsSuggest(source, mappingData);
            }else if(mappingData.isKeyword() && mappingData.isIncludeInParent()){
            }
            if(i == mappingDataList.length - 1){
                source.append(" }\n");
            }else{
                source.append(" },\n");
            }
        }
        source.append(" }\n");
        source.append(" }\n");
        source.append(" }\n");
        return isAutocomplete;
    }

    private void keyWord(StringBuffer source, MappingData mappingData) {
        source.append(" \n");
        source.append(" ,\"fields\": {\n");
        source.append(" \"keyword\": {\n");
        source.append(" \"type\": \"keyword\",\n");
        source.append(" \"ignore_above\": "+mappingData.getIgnoreAbove());
        source.append(" }\n");
        source.append(" }\n");
    }

    private void noKeyWordAndIsSuggest(StringBuffer source, MappingData mappingData) {
        source.append(" \n");
        source.append(" ,\"fields\": {\n");
        source.append(" \"suggest\": {\n");
        source.append(" \"type\": \"completion\",\n");
        source.append(" \"analyzer\": \""+mappingData.getAnalyzer()+"\"\n");
        source.append(" }\n");
        source.append(" }\n");
    }

    private void keywordAndIsSuggest(StringBuffer source, MappingData mappingData) {
        source.append(" \n");
        source.append(" ,\"fields\": {\n");

        source.append(" \"keyword\": {\n");
        source.append(" \"type\": \"keyword\",\n");
        source.append(" \"ignore_above\": "+mappingData.getIgnoreAbove());
        source.append(" },\n");

        source.append(" \"suggest\": {\n");
        source.append(" \"type\": \"completion\",\n");
        source.append(" \"analyzer\": \""+mappingData.getAnalyzer()+"\"\n");
        source.append(" }\n");

        source.append(" }\n");
    }

    private boolean textOrKeyword(MappingData mappingData) {
        return "text".equals(mappingData.getDatatype()) || "keyword".equals(mappingData.getDatatype());
    }

    @Override
    public void dropIndex(Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexname();
        DeleteIndexRequest request = new DeleteIndexRequest(indexname);
        IndicesClient indicesClient = client.indices();
        indicesClient.delete(request, RequestOptions.DEFAULT);
    }

    @Override
    public boolean exists(Class<T> clazz) throws Exception{
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexname();
        String indextype = metaData.getIndextype();
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexname);
        request.types(indextype);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }
}
