package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Description 用户手机号修改记录表
 * @Date  2020/3/11 0011 14:25
 */
@Data
@TableName("catering_merchant")
public class CateringUserUpdateRecordEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = 116397391775196435L;
     /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 旧手机号
     */
    @TableField(value = "old_phone")
    private String oldPhone;
    /**
     * 新手机号
     */
    @TableField(value = "new_phone")
    private String newPhone;
     /**
     * 0-否 1-是
     */
     @TableField(value = "is_del")
    private Boolean del;
     /**
     * 创建人
     */
     @TableField(value = "create_by")
    private Long createBy;
     /**
     * 创建时间
     */
     @TableField(value = "create_time")
    private LocalDateTime createTime;
     /**
     * 修改人
     */
     @TableField(value = "update_by")
    private Long updateBy;
     /**
     * 修改时间
     */
     @TableField(value = "update_time")
    private LocalDateTime updateTime;
    }