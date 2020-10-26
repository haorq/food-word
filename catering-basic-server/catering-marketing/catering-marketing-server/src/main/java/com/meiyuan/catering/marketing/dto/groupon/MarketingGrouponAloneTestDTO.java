package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/09/23 09:09
 * @description 修改团购时间并同步ES（测试专用）
 **/

@Data
@ApiModel(value = "修改团购时间并同步ES（测试专用）")
public class MarketingGrouponAloneTestDTO {

    @ApiModelProperty(value = "团购活动ID")
    @NotNull(message = "团购活动ID不能为空")
    private Long id;
    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "令牌（固定）")
    @NotBlank(message = "令牌不能为空")
    private String token;

}
