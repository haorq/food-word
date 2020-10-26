package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/09/02 17:09
 * @description 营销特价商品表实体
 **/

@Data
@TableName("catering_marketing_special")
public class CateringMarketingSpecialEntity extends IdEntity {

    /**
     * 商户ID
     */
    @TableField(value = "merchant_id")
    private Long merchantId;

    /**
     * 门店ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 活动名称
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
     * 活动描述
     */
    @TableField(value = "description", updateStrategy = FieldStrategy.IGNORED)
    private String description;

    /**
     * 活动目的
     */
    @TableField(value = "activity_target", updateStrategy = FieldStrategy.IGNORED)
    private String activityTarget;

    /**
     * 拉新目标
     */
    @TableField(value = "user_target", updateStrategy = FieldStrategy.IGNORED)
    private Integer userTarget;

    /**
     * 增长营业额目标
     */
    @TableField(value = "business_target", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal businessTarget;

    /**
     * 定价方式 1-统一折扣 2-折扣 3-固定价
     */
    @TableField(value = "fix_type")
    private Integer fixType;

    /**
     * 统一折扣(当定价方式为统一折扣时有值)
     */
    @TableField(value = "unify_special_number", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal unifySpecialNumber;

    /**
     * 上下架状态 1-下架(冻结) 2-上架
     */
    @TableField(value = "up_down")
    private Integer upDown;

    /**
     * 进行状态 1-未开始 2-进行中 3-已结束
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 是否删除 0-否 1-是
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
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private Long updateBy;

}
