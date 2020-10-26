package com.meiyuan.catering.marketing.vo.recommend;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/16
 **/
@Data
@ApiModel("团购列表VO")
public class RecommendPrizeListVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("活动ID")
    private Long id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("活动状态（1：未开始，2：进行中，3：已结束）")
    private Integer status;

    @ApiModelProperty("奖品类型")
    private String prizeType;

}
