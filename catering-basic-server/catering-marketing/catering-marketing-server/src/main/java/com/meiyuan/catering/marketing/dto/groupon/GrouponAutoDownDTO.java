package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/4/1
 **/
@Data
@ApiModel("自动下架团购活动DTO")
public class GrouponAutoDownDTO {

    @ApiModelProperty("团购活动ID")
    private Long id;

    @ApiModelProperty("团购活动结束时间")
    private LocalDateTime endTime;
}
