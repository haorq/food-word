package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分记录表(CateringIntegralRecord)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-10 14:23:01
 */
@Data
@TableName("catering_integral_record")
public class CateringIntegralRecordEntity extends IdEntity {
    private static final long serialVersionUID = -27824037409360686L;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户昵称
     */
    private String userNickName;
    /**
     * 积分
     */
    private Integer integral;
    /**
     * 积分规则编码
     */
    private String integralNo;
    /**
     * 积分来源或消费说明
     */
    private String reason;
    /**
     * 用户手机号
     */
    private String userPhone;
    /**
     * 用户类型 1：企业用户，2：个人用户
     */
    private Integer userType;
    /**
     * 1:增加 2:减少
     */
    private Integer type;
    /**
     * 积分过期时间 过期新增一条过期记录
     */
    private LocalDateTime pastTime;
    /**
     * 软删除标记，0为正常，1为删除
     */
    @TableLogic
    @TableField(value = "is_del")
    private Boolean del;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
