package com.meiyuan.catering.marketing.vo.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/07 10:08
 * @description 秒杀活动场次详情VO
 **/

@Data
@ApiModel(value = "秒杀活动场次详情VO")
public class MarketingSeckillDetailEventVO {

    @ApiModelProperty(value = "秒杀场次ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long eventId;

    @ApiModelProperty("秒杀场次时间")
    private String eventTime;

}
