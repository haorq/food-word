package com.meiyuan.catering.finance.query.recharge;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/18
 */
@Data
@ApiModel("充值活动列表查询DTO")
public class RechargeActivityQueryDTO extends BasePageDTO {

    @ApiModelProperty("开始时间 yyyy-MM-dd")
    private LocalDateTime beginTime;
    @ApiModelProperty("结束时间 yyyy-MM-dd")
    private LocalDateTime endTime;
    @ApiModelProperty("活动名称")
    private String name;
    @ApiModelProperty("状态,0--进行中  1:未开始  2:已结束")
    private Integer status;
    @ApiModelProperty("是否上架 true:是  false:否")
    private Boolean up;

}
