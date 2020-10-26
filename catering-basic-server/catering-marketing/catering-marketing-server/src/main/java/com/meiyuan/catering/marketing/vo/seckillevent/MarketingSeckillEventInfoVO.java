package com.meiyuan.catering.marketing.vo.seckillevent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/12 18:08
 * @description 秒杀场次信息VO
 **/

@Data
@ApiModel(value = "秒杀场次信息VO")
public class MarketingSeckillEventInfoVO {

    @ApiModelProperty(value = "秒杀关系ID")
    private Long id;
    @ApiModelProperty(value = "秒杀场次ID")
    private Long eventId;
    @ApiModelProperty(value = "秒杀活动ID")
    private Long seckillId;
    @ApiModelProperty(value = "营销商品ID")
    private Long mGoodsId;

}
