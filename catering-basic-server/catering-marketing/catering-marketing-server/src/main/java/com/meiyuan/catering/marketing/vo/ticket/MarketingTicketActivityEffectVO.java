package com.meiyuan.catering.marketing.vo.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName MarketingTicketActivityEffectVO
 * @Description
 * @Author gz
 * @Date 2020/8/5 15:36
 * @Version 1.3.0
 */
@Data
public class MarketingTicketActivityEffectVO {

    private Long id;
    @ApiModelProperty(value = "预计成本")
    private BigDecimal predictCost;
    @ApiModelProperty(value = "实际成本")
    private BigDecimal practicalCost;
    @ApiModelProperty(value = "目标拉新人数")
    private Integer targetMember;
    @ApiModelProperty(value = "实际拉新人数")
    private Integer nowMember;
    @ApiModelProperty(value = "目标增长营业额")
    private Integer targetTurnover;
    @ApiModelProperty(value = "实际增长营业额")
    private BigDecimal nowTurnover;
    @ApiModelProperty(value = "整体活动数据")
    private List<TicketActivityOrderVO> activityList;


}
