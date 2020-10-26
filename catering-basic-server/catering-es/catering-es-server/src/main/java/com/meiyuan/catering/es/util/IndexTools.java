package com.meiyuan.catering.es.util;


import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.annotation.ESMetaData;

import java.lang.reflect.Field;

/**
 * @program: esdemo
 * @description: 索引信息操作工具类
 * @author: X-Pacific zhang
 * @create: 2019-01-29 14:29
 **/
public class IndexTools {
    /**
     * 获取索引元数据：indexname、indextype
     * @param clazz
     * @return
     */
    public static MetaData getIndexType(Class<?> clazz){
        String indexname = "";
        String indextype = "";
        if(clazz.getAnnotation(ESMetaData.class) != null){
            indexname = clazz.getAnnotation(ESMetaData.class).indexName();
            indextype = clazz.getAnnotation(ESMetaData.class).indexType();
            MetaData metaData = new MetaData(indexname,indextype);
            metaData.setPrintLog(clazz.getAnnotation(ESMetaData.class).printLog());
            if(Tools.arrayIsNull(clazz.getAnnotation(ESMetaData.class).searchIndexNames())) {
                metaData.setSearchIndexNames(new String[]{indexname});
            }else{
                metaData.setSearchIndexNames((clazz.getAnnotation(ESMetaData.class).searchIndexNames()));
            }
            return metaData;
        }
        return null;
    }

    /**
     * 获取索引元数据：主分片、备份分片数的配置
     * @param clazz
     * @return
     */
    public static MetaData getShardsConfig(Class<?> clazz){
        int numberOfShards = 0;
        int numberOfReplicas = 0;
        if(clazz.getAnnotation(ESMetaData.class) != null){
            numberOfShards = clazz.getAnnotation(ESMetaData.class).number_of_shards();
            numberOfReplicas = clazz.getAnnotation(ESMetaData.class).number_of_replicas();
            MetaData metaData = new MetaData(numberOfShards,numberOfReplicas);
            metaData.setPrintLog(clazz.getAnnotation(ESMetaData.class).printLog());
            return metaData;
        }
        return null;
    }

    /**
     * 获取索引元数据：indexname、indextype、主分片、备份分片数的配置
     * @param clazz
     * @return
     */
    public static MetaData getMetaData(Class<?> clazz){
        String indexname = "";
        String indextype = "";
        int numberOfShards = 0;
        int numberOfReplicas = 0;
        if(clazz.getAnnotation(ESMetaData.class) != null){
            indexname = clazz.getAnnotation(ESMetaData.class).indexName();
            indextype = clazz.getAnnotation(ESMetaData.class).indexType();
            numberOfShards = clazz.getAnnotation(ESMetaData.class).number_of_shards();
            numberOfReplicas = clazz.getAnnotation(ESMetaData.class).number_of_replicas();
            MetaData metaData = new MetaData(indexname,indextype,numberOfShards,numberOfReplicas);
            metaData.setPrintLog(clazz.getAnnotation(ESMetaData.class).printLog());
            if(Tools.arrayIsNull(clazz.getAnnotation(ESMetaData.class).searchIndexNames())) {
                metaData.setSearchIndexNames(new String[]{indexname});
            }else{
                metaData.setSearchIndexNames((clazz.getAnnotation(ESMetaData.class).searchIndexNames()));
            }
            return metaData;
        }
        return null;
    }

    /**
     * 获取配置于Field上的mapping信息，如果未配置注解，则给出默认信息
     * @param field
     * @return
     */
    public static MappingData getMappingData(Field field){
        if(field == null){
            return null;
        }
        field.setAccessible(true);
        MappingData mappingData = new MappingData();
        mappingData.setFieldName(field.getName());
        if(field.getAnnotation(ESMapping.class) != null){
            ESMapping esMapping = field.getAnnotation(ESMapping.class);
            mappingData.setDatatype(esMapping.datatype().toString().replaceAll("_type",""));
            mappingData.setAnalyzer(esMapping.analyzer().toString());
            mappingData.setAutocomplete(esMapping.autocomplete());
            mappingData.setIgnoreAbove(esMapping.ignore_above());
            mappingData.setSearchAnalyzer(esMapping.search_analyzer().toString());
            mappingData.setKeyword(esMapping.keyword());
            mappingData.setSuggest(esMapping.suggest());
            mappingData.setIncludeInParent(esMapping.includeInParent());
            mappingData.setAllowSearch(esMapping.allow_search());
            mappingData.setCopyTo(esMapping.copy_to());
        }else{
            mappingData.setDatatype("text");
            mappingData.setAnalyzer("standard");
            mappingData.setAutocomplete(false);
            mappingData.setIgnoreAbove(256);
            mappingData.setSearchAnalyzer("standard");
            mappingData.setKeyword(true);
            mappingData.setSuggest(false);
            mappingData.setIncludeInParent(false);
            mappingData.setAllowSearch(true);
            mappingData.setCopyTo("");
        }
        return mappingData;
    }

    /**
     * 批量获取配置于Field上的mapping信息，如果未配置注解，则给出默认信息
     * @param clazz
     * @return
     */
    public static MappingData[] getMappingData(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        MappingData[] mappingDataList = new MappingData[fields.length];
        for (int i = 0; i < fields.length; i++) {
            if("serialVersionUID".equals(fields[i].getName())){
                continue;
            }
            mappingDataList[i] = getMappingData(fields[i]);
        }
        return mappingDataList;
    }

}
