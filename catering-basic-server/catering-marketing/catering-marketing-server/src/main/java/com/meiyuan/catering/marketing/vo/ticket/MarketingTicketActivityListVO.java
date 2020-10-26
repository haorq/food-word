package com.meiyuan.catering.marketing.vo.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName MarketingTicketListVO
 * @Description 营销券列表VO
 * @Author gz
 * @Date 2020/8/5 14:36
 * @Version 1.3.0
 */
@Data
public class MarketingTicketActivityListVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("ID")
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("活动ID--平台补贴此数据有值")
    private Long activityId;
    /**
     * 活动类型：1-店内领券；2-店外发券
     */
    @ApiModelProperty(value = "活动类型：1-店内领券；2-店外发券；3-平台补贴")
    private Integer activityType;
    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;
    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
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
     * 目标拉新用户数
     */
    @ApiModelProperty(value = "当前完成拉新用户数")
    private Integer nowMember;
    /**
     * 目标增长营业额
     */
    @ApiModelProperty(value = "当前完成增长营业额")
    private BigDecimal nowTurnover;

    @ApiModelProperty(value = "使用门店")
    private String shop;
    @ApiModelProperty(value = "剩余天数")
    private Integer residueDays;
    @ApiModelProperty(value = "上下架状态：1-下架(已冻结)；2-上架")
    private Integer upDownStatus;

    @ApiModelProperty(value = "平台承担优惠成本")
    private BigDecimal bearDuty;
}
