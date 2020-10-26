package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商户审核表(CateringMerchantAudit)实体类
 *
 * @author wxf
 * @since 2020-03-10 10:12:31
 */
@Data
@TableName("catering_merchant_audit")
public class CateringMerchantAuditEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = 738990652941868149L;
     /**
     * 商户id
     */
     @TableField(value = "merchant_id")
    private Long merchantId;
    /**
     * 营业执照
     */
    @TableField(value = "business_license")
    private String businessLicense;
    /**
     * 卫生许可证
     */
    @TableField(value = "hygiene_license")
    private String hygieneLicense;
     /**
     * 审核状态:1:待上传，2：未审核，3：已通过，4：未通过
     */
     @TableField(value = "audit_status")
    private Integer auditStatus;
     /**
     * 未通过理由
     */
     @TableField(value = "reasons_for_failure")
    private String reasonsForFailure;
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