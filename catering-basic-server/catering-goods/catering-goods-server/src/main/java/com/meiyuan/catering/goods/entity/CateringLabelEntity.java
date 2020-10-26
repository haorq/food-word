package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签表(CateringLabel)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:39:22
 */
@Data
@TableName("catering_label")
public class CateringLabelEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 915649398412350765L;
    /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
    /**
     * 标签名字
     */
    @TableField(value = "label_name")
    private String labelName;
    /**
     * 标签描述
     */
    @TableField(value = "label_describe")
    private String labelDescribe;
    /**
     * 默认标签 1-新增 2-默认
     */
    @TableField(value = "default_label")
    private Integer defaultLabel;

    /**
     * 1-禁用 2-启用
     */
    @TableField(value = "label_status")
    private Integer labelStatus;
    /**
     * 0-否 1-是
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    private Boolean del;
    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}