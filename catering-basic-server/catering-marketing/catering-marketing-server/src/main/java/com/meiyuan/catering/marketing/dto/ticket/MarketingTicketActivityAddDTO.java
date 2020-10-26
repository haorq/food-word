package com.meiyuan.catering.marketing.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName MarketingTicketActivityAddDTO
 * @Description 营销优惠券活动新增DTO
 * @Author gz
 * @Date 2020/8/5 14:03
 * @Version 1.3.0
 */
@Data
public class MarketingTicketActivityAddDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(hidden = true)
    private Long merchantId;
    /**
     * 活动名称
     */
    @NotBlank(message = "活动名称不能为空")
    @ApiModelProperty(value = "活动名称")
    private String activityName;
    /**
     * 活动开始时间
     */
    @NotNull(message = "活动开始时间不能为空")
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    /**
     * 活动结束时间
     */
    @NotNull(message = "活动结束时间不能为空")
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    /**
     * 生效周期：0-表示每天；其他表示具体的星期
     */
    @ApiModelProperty(value = "生效周期：0-表示每天；其他表示具体的星期")
    private String effectiveData;
    /**
     * 活动目的
     */
    @ApiModelProperty(value = "活动目的")
    private String target;
    /**
     * 目标拉新用户数
     */
    @ApiModelProperty(value = "目标拉新用户数")
    private Integer targetMember;
    /**
     * 目标增长营业额
     */
    @ApiModelProperty(value = "目标增长营业额")
    private Integer targetTurnover;
    /**
     * 活动类型：1-店内领券；2-店外发券
     */
    @ApiModelProperty(value = "活动类型：1-店内领券；2-店外发券")
    private Integer activityType;
    @NotNull(message = "至少添加一个满减券")
    @ApiModelProperty(value = "券信息集合")
    private List<TicketBasicDTO> ticketList;
    @ApiModelProperty(value = "门店集合")
    private List<Long> shopList;

}
