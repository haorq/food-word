package com.meiyuan.catering.marketing.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 13:38
 */
@Data
public class ActivityRuleVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "规则 id")
    private Long ruleId;

    @ApiModelProperty(value = "邀请新人注册成功完成条件")
    private Integer conditionsRule;

    @ApiModelProperty(value = "评价规则 1:仅图片 2:仅文字 3:图片加文字")
    private Integer evaluateRule;

    @ApiModelProperty(value = "领取限制")
    private Integer receiveRestrict;

    @ApiModelProperty(value = "积分数")
    private Integer givePoints;

    @ApiModelProperty(value = "被分享人积分数")
    private Integer passiveGivePoints;

    @ApiModelProperty(value = "有效期 1:永久有效")
    private Integer validDate;

    @ApiModelProperty(value = "关联的优惠券")
    private List<RuleTicketRelationVO> ruleTicketRelationList;
}
