package com.meiyuan.catering.es.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.enums.DataType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wxf
 * @date 2020/3/23 13:43
 * @description 商品分类/标签信息
 **/
@Data
@ApiModel("商品分类/标签信息模型")
public class EsGoodsCategoryAndLabelDTO {
    @ApiModelProperty("分类还是标签 1-分类 2-标签")
    private Integer type;
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ESMapping(datatype = DataType.keyword_type)
    private Long id;
    @ApiModelProperty("名称")
    @ESMapping(datatype = DataType.keyword_type)
    private String name;
    public EsGoodsCategoryAndLabelDTO(){}
    public EsGoodsCategoryAndLabelDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
