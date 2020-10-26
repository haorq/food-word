package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * description：平台活动（catering_marketing_activity）实体表
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 10:55
 */
@Data
@TableName("catering_marketing_activity")
public class CateringMarketingActivityEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 202008081121110501L;

    /**
     * 商户id
     */
    @TableField(value = "merchant_id",fill = FieldFill.INSERT)
    private Long merchantId;

    /**
     * 活动编码
     */
    @TableField(value = "activity_number")
    private String activityNumber;

    /**
     * 活动名称
     */
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
     * 对象类型 1:用户 2 品牌
     */
    @TableField(value = "target_type")
    private Integer targetType;

    /**
     * 活动对象: 是用户则（ 0:所有用户 1:个人用户 2:企业用户），是品牌则 （0:所有品牌 1:自营品牌 2:非自营品牌）
     */
    private Integer target;

    /**
     * 活动类型 1:新用户注册 2:推荐有奖 3:首单奖励 4:评价赠礼 5:平台补贴 6:发券宝
     */
    @TableField(value = "activity_type")
    private Integer activityType;

    /**
     * 发放条件 1:立即 2:任务完成后自动发送 3:手动领取
     */
    @TableField(value = "release_conditions")
    private Integer releaseConditions;

    /**
     * 是否发放积分 0:否  1:是
     */
    @TableField(value = "is_points")
    private Boolean points;

    /**
     * 是否发放优惠券 0:否  1:是
     */
    @TableField(value = "is_ticket")
    private Boolean ticket;

    /**
     * 平台承担责任成本
     */
    @TableField(value = "bear_duty")
    private BigDecimal bearDuty;

    /**
     * 状态 1:下架(冻结) 2:上架
     */
    private Integer state;

    /**
     * 删除标记
     */
    @TableField(value = "is_del")
    private Boolean del;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;


}
