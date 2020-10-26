package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分活动
 *
 * @author zengzhangni
 * @date 2020-03-18
 */
@Data
@TableName("catering_integral_activity")
public class CateringIntegralActivityEntity extends IdEntity {

    /**
     * 活动名称
     */
    private String name;
    /**
     * 积分规则编码
     */
    private String integralNo;
    /**
     * 积分规则名称
     */
    private String integralName;
    /**
     * 积分数量
     */
    private Integer integral;
    /**
     * 积分有效期 null:长期
     */
    private Integer validity;
    /**
     * 积分对象 0:所有 1:企业 2:个人
     */
    private Integer userType;
    /**
     * 积分说明
     */
    private String remark;
    /**
     * 状态，0为正常，1为禁用
     */
    private Integer status;
    /**
     * 删除标记（0：未删除[默认]；1：已删除）
     */
    @TableLogic
    @TableField("is_del")
    private Boolean del;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
