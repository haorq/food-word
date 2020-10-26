package com.meiyuan.catering.es.dto.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/18 19:08
 * @description 小程序首页秒杀场次提醒DTO
 **/

@Data
@ApiModel(value = "小程序首页秒杀场次提醒DTO")
public class EsMarketingSeckillEventHintDTO {

    @ApiModelProperty(value = "城市编码")
    private String cityCode;
    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户")
    private Integer userType;

}
