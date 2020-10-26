package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class MerchantParamDto {

    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("时间查询类型 1：自然日；2：营业日")
    private Integer type;
    @ApiModelProperty("时间查询类型 1：本日；2：本周；3：本月")
    private Integer queryType;
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}
