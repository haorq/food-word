package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/09/23 13:09
 * @description 团购活动结束DTO
 **/

@Data
@ApiModel(value = "团购活动结束DTO")
public class MarketingGrouponUpDownDTO {

    @ApiModelProperty(value = "团购活动ID")
    private Long id;
    @ApiModelProperty(value = "团购活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "团购活动状态")
    private Integer upDown;

}
