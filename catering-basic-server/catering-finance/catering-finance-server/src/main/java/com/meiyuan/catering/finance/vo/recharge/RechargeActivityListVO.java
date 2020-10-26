package com.meiyuan.catering.finance.vo.recharge;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/19
 */
@Data
@ApiModel("充值活动列表查询VO")
public class RechargeActivityListVO extends IdEntity {

    @ApiModelProperty("活动名称")
    private String name;
    @ApiModelProperty("活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty("活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty("活动对象 1:企业 2:个人")
    private Integer userType;
    @ApiModelProperty("0--进行中  1:未开始  2:已结束")
    private Integer status;
    @ApiModelProperty("是否上架 true:是  false:否")
    private Boolean up;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
