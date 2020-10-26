package com.meiyuan.catering.marketing.dto.ticket;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName MarketingPlatFormActivityParamDTO
 * @Description
 * @Author gz
 * @Date 2020/8/8 17:06
 * @Version 1.3.0
 */
@Data
public class MarketingPlatFormActivityParamDTO extends BasePageDTO {

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动名称")
    private String activityName;

}
