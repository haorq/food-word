package com.meiyuan.catering.marketing.dto.recommend;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/20
 **/
@Data
@ApiModel("推荐有奖活动DTO")
public class RecommendPrizeDTO {

    @ApiModelProperty("活动ID")
    private Long id;

    @NotBlank(message = "活动名称不能为空")
    @ApiModelProperty("活动名称")
    private String name;

    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @NotNull(message = "推荐条件不能为空")
    @ApiModelProperty("推荐条件（1：新人注册成功，2：下单成功）")
    private Integer recommendCondition;

    @NotNull(message = "奖品类型不能为空")
    @ApiModelProperty("奖品类型（1：积分，2：优惠券）")
    private Integer prizeType;

    @ApiModelProperty("推荐人奖励积分")
    private Integer referrerIntegral;

    @ApiModelProperty("被推荐人奖励积分")
    private Integer referralIntegral;

    @ApiModelProperty("推荐人优惠券ID")
    private Long referrerTicketId;

    @ApiModelProperty("被推荐人优惠券ID")
    private Long referralTicketId;

    @ApiModelProperty("活动说明")
    private String description;
}
