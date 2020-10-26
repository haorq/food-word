package com.meiyuan.catering.es.vo.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/08/08 09:08
 * @description 小程序限时秒杀场次列表VO
 **/

@Data
@ApiModel(value = "小程序限时秒杀场次列表VO")
public class EsMarketingSeckillEventListVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "秒杀场次ID")
    private Long eventId;
    @ApiModelProperty(value = "秒杀场次时间（已开抢场次，或者时最近的未开抢场次）")
    private String eventTime;
    @ApiModelProperty(value = "秒杀场次结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "秒杀场次开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "秒杀活动场次状态 3：已开枪  4：正在疯抢  2：即将开抢  6：明日预告")
    private Integer status;
    @ApiModelProperty(value = "活动倒计时(秒)")
    private Long countDown;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "唯一的场次ID")
    private Long onlyEventId;

}
