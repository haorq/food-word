package com.meiyuan.catering.es.dto.geo;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;

/**
 * @author wxf
 * @date 2020/6/2 16:22
 * @description geo经纬度排序分类查询参数DTO
 **/
@Data
public class GeoLimitQueryDTO extends BasePageDTO {
    @ApiModelProperty("维度")
    private Double lat;
    @ApiModelProperty("经度")
    private Double lng;
    @ApiModelProperty("queryBuilder")
    private BoolQueryBuilder queryBuilder;

    public GeoLimitQueryDTO() {
    }

    public GeoLimitQueryDTO(Double lat, Double lng, Long pageNo, Long pageSize, BoolQueryBuilder queryBuilder) {
        this.lat = lat;
        this.lng = lng;
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
        this.queryBuilder = queryBuilder;
    }
}
