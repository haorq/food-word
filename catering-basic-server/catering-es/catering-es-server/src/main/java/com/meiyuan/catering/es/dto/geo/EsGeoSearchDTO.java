package com.meiyuan.catering.es.dto.geo;

import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.es.repository.Sort;
import lombok.Data;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * @author wxf
 * @date 2020/4/21 11:26
 * @description 简单描述
 **/
@Data
public class EsGeoSearchDTO<T> extends BasePageDTO {
    private Class<T> clazz;
    /**
     * 维度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lon;
    /**
     * 查询条件
     */
    private BoolQueryBuilder queryBuilder;
    /**
     * 查询字段
     */
    private String location;
    /**
     * 搜索的半径范围
     */
    private Double distance;
    /**
     * 搜索的半径范围的单位
     */
    private DistanceUnit distanceUnit;
    /**
     * 排序（默认升序）
     */
    private SortOrder sortOrder;
    /**
     * 搜索索引
     */
    private String[] index;
    /**
     * 排序字段
     */
    private Sort sort;
}
