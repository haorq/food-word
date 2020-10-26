package com.meiyuan.catering.es.util;

import lombok.Data;

/**
 * 元数据载体类
 * @author: wxf
 */

@Data
public class MetaData{
    String indexname = "";
    String indextype = "";
    int numberOfShards;
    int numberOfReplicas;
    boolean printLog = false;
    String[] searchIndexNames;
    public MetaData(String indexname, String indextype) {
        this.indexname = indexname;
        this.indextype = indextype;
    }
    public MetaData(String indexname, String indextype, int numberOfShards, int numberOfReplicas) {
        this.indexname = indexname;
        this.indextype = indextype;
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
    }

    public MetaData(int numberOfShards, int numberOfReplicas) {
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
    }
}
