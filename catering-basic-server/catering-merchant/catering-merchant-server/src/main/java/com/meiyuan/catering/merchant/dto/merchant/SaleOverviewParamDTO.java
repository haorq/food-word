package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
 * @author fql
 */
@Data
public class SaleOverviewParamDTO {

    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("时间查询类型：1：今日，2：昨日，3：自定义")
    private Integer type;
    @ApiModelProperty
    private LocalDate startTime;
    @ApiModelProperty
    private LocalDate endTime;


}
