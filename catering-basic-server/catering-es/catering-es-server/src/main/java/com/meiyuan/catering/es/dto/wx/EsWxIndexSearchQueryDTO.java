package com.meiyuan.catering.es.dto.wx;

import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.util.GpsCoordinateUtils;
import com.meiyuan.catering.es.util.EsUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/4/14 11:01
 * @description 简单描述
 **/
@Data
@ApiModel("微信首页搜索查询参数模型")
public class EsWxIndexSearchQueryDTO extends BasePageDTO {
    @ApiModelProperty("关键字")
    private String name;
    @ApiModelProperty("纬度")
    private Double lat;
    @ApiModelProperty("经度")
    private Double lng;
    @ApiModelProperty("市编码")
    private String cityCode;
}
