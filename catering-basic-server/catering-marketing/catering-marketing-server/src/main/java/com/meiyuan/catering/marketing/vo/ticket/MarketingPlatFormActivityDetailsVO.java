package com.meiyuan.catering.marketing.vo.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName MarketingPlatFormActivityDetailsVO
 * @Description 平台发券活动详情
 * @Author gz
 * @Date 2020/8/9 16:39
 * @Version 1.3.0
 */
@Data
public class MarketingPlatFormActivityDetailsVO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
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
    private List<PlatFormTicketBaseInfoVO> ticketItem;
}
