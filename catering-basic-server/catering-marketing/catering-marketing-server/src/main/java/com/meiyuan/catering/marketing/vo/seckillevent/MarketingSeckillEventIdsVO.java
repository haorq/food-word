package com.meiyuan.catering.marketing.vo.seckillevent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/09/02 10:09
 * @description 秒杀场次ID集合VO
 **/

@Data
@ApiModel(value = "秒杀场次ID集合VO")
public class MarketingSeckillEventIdsVO {

    @ApiModelProperty(value = "秒杀活动ID")
    private Long seckillId;
    @ApiModelProperty(value = "秒杀活动场次ID集")
    private String seckillEventIds;

}
