package com.meiyuan.catering.marketing.vo.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName MarketingPlatFormActivityEffectVO
 * @Description
 * @Author gz
 * @Date 2020/8/10 10:40
 * @Version 1.3.0
 */
@Data
public class MarketingPlatFormActivityEffectVO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long activityId;
    @ApiModelProperty(value = "活动名称")
    private String activityName;
    @ApiModelProperty(value = "推送时间")
    private LocalDateTime pushTime;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "平台承担优惠成本")
    private BigDecimal bearDuty;
    @ApiModelProperty(value = "领取限制")
    private Integer receiveRestrict;
    @ApiModelProperty(value = "关联优惠券数据")
    private List<PlatFormTicketOrderInfoVO> ticketItem;

    @ApiModelProperty(value = "活动成本")
    private BigDecimal activityCost;
    @ApiModelProperty(value = "商户承担成本")
    private BigDecimal merchantCost;
    @ApiModelProperty(value = "平台承担成本")
    private BigDecimal platFormCost;
    @ApiModelProperty(value = "参与门店集合")
    private List<TicketActivityShopVO> shopList;
    @ApiModelProperty(value = "参与门店ID集合",hidden = true)
    private List<Long> shopIds;
}
