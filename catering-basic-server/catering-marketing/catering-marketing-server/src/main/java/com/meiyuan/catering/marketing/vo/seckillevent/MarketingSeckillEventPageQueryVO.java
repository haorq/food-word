package com.meiyuan.catering.marketing.vo.seckillevent;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author GongJunZheng
 * @date 2020/08/04 16:08
 * @description 简单描述
 **/

@Data
@ApiModel("秒杀场次分页列表查询VO")
public class MarketingSeckillEventPageQueryVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("秒杀场次ID")
    private Long id;

    private LocalTime beginTime;

    private LocalTime endTime;

    @ApiModelProperty("秒杀场次创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("是否可以进行删除（0：否 1：是）")
    private Integer del;

    @ApiModelProperty("秒杀场次开始时间")
    public String getBeginTime() {
        return beginTime.toString();
    }

    @ApiModelProperty("秒杀场次结束时间")
    public String getEndTime() {
        return endTime.toString();
    }
}
