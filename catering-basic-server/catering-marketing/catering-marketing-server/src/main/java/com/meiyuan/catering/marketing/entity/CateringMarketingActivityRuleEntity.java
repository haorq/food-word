package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:19
 */
@Data
@TableName("catering_marketing_activity_rule")
public class CateringMarketingActivityRuleEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 202008081122110502L;

    /**
     * 活动id
     */
    @TableField(value = "activity_id")
    private Long activityId;

    /**
     * 邀请新人注册成功完成条件
     */
    @TableField(value = "conditions_rule")
    private Integer conditionsRule;

    /**
     * 评价规则 1:仅图片 2:仅文字 3:图片加文字
     */
    @TableField(value = "evaluate_rule")
    private Integer evaluateRule;

    /**
     * 领取限制
     */
    @TableField(value = "receive_restrict")
    private Integer receiveRestrict;

    /**
     * 积分数
     */
    @TableField(value = "give_points")
    private Integer givePoints;

    /**
     * 被分享人积分数
     */
    @TableField(value = "passive_give_points")
    private Integer passiveGivePoints;

    /**
     * 有效期 1:永久有效
     */
    @TableField(value = "valid_date")
    private Integer validDate;
}

