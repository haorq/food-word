package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分规则表(CateringIntegralRule)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-10 14:23:01
 */
@Data
@TableName("catering_integral_rule")
public class CateringIntegralRuleEntity extends IdEntity {
    private static final long serialVersionUID = -36003477299775491L;
    /**
     * 积分编码
     */
    private String integralNo;
    /**
     * 积分规则名称
     */
    private String name;
    /**
     * 积分规则类型 1:增加 2:减少
     */
    private Integer type;
    /**
     * 积分规则类型 0:活动 1:系统(系统类型在平台不能添加)
     */
    @TableLogic
    private Boolean ruleType;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
