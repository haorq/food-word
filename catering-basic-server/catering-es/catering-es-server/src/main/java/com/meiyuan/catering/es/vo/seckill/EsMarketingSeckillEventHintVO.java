package com.meiyuan.catering.es.vo.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/08/08 09:08
 * @description 小程序首页-优惠专区-限时秒杀提示信息VO
 **/

@Data
@ApiModel(value = "小程序首页-优惠专区-限时秒杀信息VO")
public class EsMarketingSeckillEventHintVO {

    @ApiModelProperty(value = "秒杀场次ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long eventId;
    @ApiModelProperty(value = "秒杀场次时间")
    private String eventTime;
    @ApiModelProperty(value = "秒杀场次结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "秒杀场次开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "秒杀活动状态 2：即将开抢  3：已开抢")
    private Integer status;
    @ApiModelProperty(value = "活动倒计时(秒)")
    private Long countDown;

}
