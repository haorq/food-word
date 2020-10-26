package com.meiyuan.catering.marketing.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 12:02
 */
@Data
@ApiModel("活动分页列表")
public class ActivityPageVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "活动编号")
    private String activityNumber;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "状态 4:已冻结 1:待开始 2:进行中 3:已结束")
    private Integer activityState;

    @ApiModelProperty(value = "是否发放积分 0:否  1:是")
    private Boolean points;

    @ApiModelProperty(value = "是否发放优惠券 0:否  1:是")
    private Boolean ticket;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "活动类型 1:新用户注册 2:推荐有奖 3:首单奖励 4:评价赠礼 5:平台补贴 6:发券宝")
    private Integer activityType;

    @ApiModelProperty(value = "状态 1:下架（冻结） 2:上架")
    private Integer state;
}
