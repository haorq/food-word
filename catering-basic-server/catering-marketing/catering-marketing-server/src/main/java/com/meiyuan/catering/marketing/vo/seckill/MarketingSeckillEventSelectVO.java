package com.meiyuan.catering.marketing.vo.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalTime;

/**
 * @author GongJunZheng
 * @date 2020/08/08 09:08
 * @description 秒杀场次下拉选择VO
 **/

@Data
@ApiModel(value = "秒杀场次下拉选择VO")
public class MarketingSeckillEventSelectVO {

    @ApiModelProperty(value = "秒杀场次ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "秒杀场次时间")
    private String eventTime;

    @JsonIgnore
    private LocalTime beginTime;
    @JsonIgnore
    private LocalTime endTime;
}
