package com.meiyuan.catering.es.dto.property;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/3/16 14:05
 * @description 属性新增/查看 DTO
 **/
@Data
@ApiModel("新增/查看属性模型")
public class EsGoodsPropertyDTO {
    @ApiModelProperty("属性名称")
    private String propertyTypeName;
    @ApiModelProperty("属性值集合")
    private List<String> propertyValueList;
}
