package com.meiyuan.catering.marketing.dto.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 12:01
 */
@Data
@ApiModel("活动新增/修改上传参数")
public class ActivitySaveDTO {

    @ApiModelProperty(value = "id 无-新增，有-修改")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "活动名称")
    @NotBlank(message = "活动名称不能为空！")
    private String name;

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空！")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空！")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "对象类型 1:用户 2 品牌")
    private Integer targetType;

    @ApiModelProperty(value = "活动对象: 是用户则（ 0:所有用户 1:个人用户 2:企业用户），是品牌则 （0:所有品牌 1:自营品牌 2:非自营品牌）")
    @NotNull(message = "活动对象不能为空！")
    private Integer target;

    @ApiModelProperty(value = "活动类型 1:新用户注册 2:推荐有奖 3:首单奖励 4:评价赠礼 5:平台补贴 6:发券宝")
    @NotNull(message = "活动类型不能为空！")
    private Integer activityType;

    @ApiModelProperty(value = "发放条件 1:立即 2:任务完成后自动发送 3:手动领取")
    private Integer releaseConditions;

    @ApiModelProperty(value = "是否发放积分 0:否  1:是")
    @NotNull(message = "发放权益不能为空！")
    private Boolean points;

    @ApiModelProperty(value = "是否发放优惠券 0:否  1:是")
    @NotNull(message = "发放权益不能为空！")
    private Boolean ticket;

    @ApiModelProperty(value = "平台承担责任成本")
    private BigDecimal bearDuty;

    @ApiModelProperty(value = "状态 1:下架(冻结) 2:上架 ")
    private Integer state;

    @ApiModelProperty(value = "营销方式（规则）")
    @NotEmpty(message = "营销方式不能为空！")
    private List<ActivityRuleDTO> activityRuleList;
}
