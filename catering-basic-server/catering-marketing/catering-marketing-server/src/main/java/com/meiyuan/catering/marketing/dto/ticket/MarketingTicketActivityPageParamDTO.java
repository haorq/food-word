package com.meiyuan.catering.marketing.dto.ticket;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName MarketingTicketActivityPageParamDTO
 * @Description
 * @Author gz
 * @Date 2020/8/5 14:43
 * @Version 1.3.0
 */
@Data
public class MarketingTicketActivityPageParamDTO extends BasePageDTO {
    /**
     * 活动类型：1-店内领券；2-店外发券
     */
    @ApiModelProperty(value = "活动类型：1-店内领券；2-店外发券")
    private Integer activityType;
    @ApiModelProperty(value = "门店ID")
    private Long shopId;
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动名称")
    private String activityName;
    @ApiModelProperty(value = "活动状态：1-未开始；2-进行中；3-已结束")
    private Integer status;
    @ApiModelProperty(value = "活动来源：1-平台；2-品牌")
    private Integer source;
    @ApiModelProperty(hidden = true)
    private Long merchantId;
}
