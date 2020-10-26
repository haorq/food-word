package com.meiyuan.catering.marketing.vo.seckillevent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalTime;

/**
 * @author GongJunZheng
 * @date 2020/08/10 10:08
 * @description 秒杀活动场次VO
 **/

@Data
@ApiModel(value = "秒杀活动场次VO")
public class MarketingSeckillEventVO extends IdEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "秒杀ID")
    private Long seckillId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "秒杀场次ID")
    private Long eventId;
    @ApiModelProperty(value = "秒杀场次时间段")
    private String eventTime;
    @ApiModelProperty(value = "秒杀场次开始时间")
    private LocalTime eventBeginTime;
    @ApiModelProperty(value = "秒杀场次结束时间")
    private LocalTime eventEndTime;

}
