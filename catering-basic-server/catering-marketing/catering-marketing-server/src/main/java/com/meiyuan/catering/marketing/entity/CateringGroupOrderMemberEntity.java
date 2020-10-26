package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 团单成员表(CateringGroupOrderMember)实体类
 *
 * @author wxf
 * @since 2020-03-10 11:20:34
 */
@Data
@TableName("catering_group_order_member")
public class CateringGroupOrderMemberEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = -35663358087452093L;
    /**
     * 团单数据表主键id
     */
    @TableField(value = "group_order_id")
    private Long groupOrderId;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 成员id
     */
    @TableField(value = "member_id")
    private Long memberId;
    /**
     * 成员名称
     */
    @TableField(value = "member_name")
    private String memberName;
    /**
     * 成员昵称
     */
    @TableField(value = "member_nick_name")
    private String memberNickName;
    /**
     * 是否发起人
     */
    @TableField(value = "is_sponsor")
    private Boolean isSponsor;
    /**
     * 删除标记
     */
    @TableField(value = "is_del")
    @TableLogic
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