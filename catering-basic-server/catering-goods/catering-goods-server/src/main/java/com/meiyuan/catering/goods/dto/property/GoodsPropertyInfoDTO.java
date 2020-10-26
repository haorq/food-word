package com.meiyuan.catering.goods.dto.property;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/4/3 11:53
 * @description 简单描述
 **/
@Data
@ApiModel("商品属性数据模型")
public class GoodsPropertyInfoDTO {
    @ApiModelProperty("属性值key 新增不传，修改必传")
    private String key;
    @ApiModelProperty("属性值")
    private String name;
    public GoodsPropertyInfoDTO(){}
    public GoodsPropertyInfoDTO(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
