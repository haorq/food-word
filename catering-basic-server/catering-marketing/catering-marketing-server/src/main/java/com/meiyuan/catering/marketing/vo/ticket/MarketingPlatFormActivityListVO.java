package com.meiyuan.catering.marketing.vo.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName MarketingPlatFormActivityListVO
 * @Description 营销平台活动分页列表VO
 * @Author gz
 * @Date 2020/8/8 14:53
 * @Version 1.3.0
 */
@Data
public class MarketingPlatFormActivityListVO {

    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "活动名称")
    private String activityName;
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "推送时间")
    private LocalDateTime pushTime;
    @ApiModelProperty(value = "状态：1-未开始；2-进行中；3-已结束")
    private Integer status;
    @ApiModelProperty(value = "平台承担优惠成本")
    private BigDecimal bearDuty;

}
