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
public class RecommendPrizeDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("活动ID")
    private Long id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("奖品类型（1：积分，2：优惠券）")
    private Integer prizeType;

    @ApiModelProperty("推荐条件（1：新人注册成功，2：下单成功）")
    private Integer recommendCondition;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("推荐人优惠券ID")
    private Long referrerTicketId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("被推荐人优惠券ID")
    private Long referralTicketId;

    @ApiModelProperty("推荐人奖励积分")
    private Integer referrerIntegral;

    @ApiModelProperty("被推荐人奖励积分")
    private Integer referralIntegral;

    @ApiModelProperty("活动说明")
    private String description;
}
