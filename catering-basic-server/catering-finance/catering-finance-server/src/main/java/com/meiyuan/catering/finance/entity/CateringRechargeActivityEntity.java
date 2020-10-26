package com.meiyuan.catering.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 充值活动
 *
 * @author zengzhangni
 * @date 2020-03-19
 */
@Data
@TableName("catering_recharge_activity")
public class CateringRechargeActivityEntity extends IdEntity {

    /**
     * 活动名称
     */
    private String name;
    /**
     * 活动开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;
    /**
     * 活动说明
     */
    private String remark;
    /**
     * 活动对象  0:所有 1:企业 2:个人
     */
    private Integer userType;
    /**
     * 状态,0--进行中  1:未开始  2:已结束
     */
    private Integer status;
    /**
     * 逻辑删除 0:正常 1:删除
     */
    @TableLogic
    @TableField("is_del")
    private Boolean del;
    /**
     * 是否上架 true:是  false:否
     */
    @TableField("is_up")
    private Boolean up;
    /**
     * 操作人Id
     */
    private Long operateId;
    /**
     * 更新人Id
     */
    private Long updateAdminId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
