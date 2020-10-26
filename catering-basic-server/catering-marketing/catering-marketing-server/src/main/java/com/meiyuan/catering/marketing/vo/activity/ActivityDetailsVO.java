package com.meiyuan.catering.marketing.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel("活动详情数据")
public class ActivityDetailsVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "创建人中文")
    private String createByStr;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "对象类型 1:用户 2 品牌")
    private Integer targetType;

    @ApiModelProperty(value = "活动对象: 是用户则（ 0:所有用户 1:个人用户 2:企业用户），是品牌则 （0:所有品牌 1:自营品牌 2:非自营品牌）")
    private Integer target;

    @ApiModelProperty(value = "活动类型 1:新用户注册 2:推荐有奖 3:首单奖励 4:评价赠礼 5:平台补贴 6:发券宝")
    private Integer activityType;

    @ApiModelProperty(value = "发放条件 1:立即 2:任务完成后自动发送 3:手动领取")
    private Integer releaseConditions;

    @ApiModelProperty(value = "是否发放积分 0:否  1:是")
    private Boolean points;

    @ApiModelProperty(value = "是否发放优惠券 0:否  1:是")
    private Boolean ticket;

    @ApiModelProperty(value = "平台承担责任成本")
    private BigDecimal bearDuty;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "营销方式（规则）")
    private List<ActivityRuleVO> activityRuleList;

    @ApiModelProperty(value = "状态 1:下架（冻结） 2:上架")
    private Integer state;

    @ApiModelProperty(value = "状态 4:已冻结 1:待开始 2:进行中 3:已结束")
    private Integer activityState;
}
