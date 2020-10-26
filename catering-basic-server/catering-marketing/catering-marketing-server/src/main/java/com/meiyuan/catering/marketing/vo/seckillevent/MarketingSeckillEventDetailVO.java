package com.meiyuan.catering.marketing.vo.seckillevent;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author GongJunZheng
 * @date 2020/08/07 10:08
 * @description 秒杀场次信息查询
 **/

@Data
@ApiModel("秒杀场次信息查询")
public class MarketingSeckillEventDetailVO {

    @ApiModelProperty(value = "秒杀场次ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "秒杀场次开始时间")
    private String beginTime;
    @ApiModelProperty(value = "秒杀场次结束时间")
    private String endTime;


}
