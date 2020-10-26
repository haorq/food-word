package com.meiyuan.catering.marketing.dto.activity;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 12:02
 */
@Data
public class ActivityPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "状态 4:已冻结 1:待开始 2:进行中 3:已结束")
    private String activityState;

    @ApiModelProperty(value = "活动类型 1:新用户注册 2:推荐有奖 3:首单奖励 4:评价赠礼 5:平台补贴 6:发券宝")
    private Integer activityType;
}
