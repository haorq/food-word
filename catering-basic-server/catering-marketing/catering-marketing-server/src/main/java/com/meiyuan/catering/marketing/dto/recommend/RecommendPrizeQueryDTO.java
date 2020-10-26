package com.meiyuan.catering.marketing.dto.recommend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/16
 **/
@Data
@ApiModel("推荐有奖查询DTO")
public class RecommendPrizeQueryDTO extends BasePageDTO {

    @ApiModelProperty("活动名称")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("活动状态（1：未开始，2：进行中，3：已结束）")
    private Integer status;

}
