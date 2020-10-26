package com.meiyuan.catering.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/3/19
 */
@Data
@ApiModel("退款统计结果VO")
public class RefundCountVO implements Serializable {

    @ApiModelProperty("所有")
    private Long all;
    @ApiModelProperty("待审核总数")
    private Long await;
    @ApiModelProperty("通过总数")
    private Long already;
    @ApiModelProperty("拒绝总数")
    private Long refuse;
}
