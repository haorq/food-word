package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/19
 * 推荐有奖活动
 **/
@Data
@TableName("catering_marketing_recommend_prize")
public class CateringMarketingRecommendPrizeEntity extends IdEntity
        implements Serializable {

    /**
     * 商户id
     */
    @TableField(value = "merchant_id",fill = FieldFill.INSERT)
    private Long merchantId;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 开始时间
     */
    @TableField(value = "begin_time")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 推荐条件（1：新人注册成功，2：下单成功）
     */
    @TableField(value = "recommend_condition")
    private Integer recommendCondition;

    /**
     * 是否删除
     */
    @TableField(value = "is_del",fill = FieldFill.INSERT)
    @TableLogic
    private Boolean del;

    /**
     * 活动说明
     */
    @TableField(value = "description")
    private String description;

    /**
     * 奖品类型（1：积分，2：优惠券）
     */
    @TableField(value = "prize_type")
    private Integer prizeType;

    /**
     * 推荐人奖励积分
     */
    @TableField(value = "referrer_integral")
    private Integer referrerIntegral;

    /**
     * 被推荐人奖励积分
     */
    @TableField(value = "referral_integral")
    private Integer referralIntegral;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 更新人
     */
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}
