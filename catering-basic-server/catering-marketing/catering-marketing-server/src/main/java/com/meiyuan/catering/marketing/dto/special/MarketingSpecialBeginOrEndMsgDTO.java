package com.meiyuan.catering.marketing.dto.special;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/09/07 13:09
 * @description 营销特价商品活动延迟消息DTO
 **/

@Data
@ApiModel(value = "营销特价商品活动延迟消息DTO")
public class MarketingSpecialBeginOrEndMsgDTO {

    @ApiModelProperty(value = "营销特价商品活动ID")
    private Long specialId;
    @ApiModelProperty(value = "状态 2-开始  3-结束")
    private Integer status;
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

}
