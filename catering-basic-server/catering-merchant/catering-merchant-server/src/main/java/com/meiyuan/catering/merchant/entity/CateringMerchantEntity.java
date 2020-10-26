package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商户表(CateringMerchant)实体类
 *
 * @author wxf
 * @since 2020-03-10 10:10:09
 */
@Data
@TableName("catering_merchant")
public class CateringMerchantEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = 116397391775196435L;

    /**
     * 商户编码
     */
    @TableField(value = "merchant_code")
     private String merchantCode;

    /**
     * 商户/品牌 名称
     */
    @TableField(value = "merchant_name")
    private String merchantName;

    /**
     * 来源：1- 商户申请 2-平台添加
     */
    @TableField(value = "merchant_source")
    private Integer merchantSource;

    /**
     * 注册电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 法人姓名
     */
    @TableField(value = "legal_person_name")
    private String legalPersonName;

    /**
     * 品牌属性:1:自营，2：非自营
     */
    @TableField(value = "merchant_attribute")
    private Integer merchantAttribute;

    /**
     * 商户服务:1;外卖小程序,2:食堂美食城（json）
     */
    @TableField(value = "merchant_service")
    private String merchantService;

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
     * 身份证正面
     */
    @TableField(value = "id_card_positive")
    private String idCardPositive;

    /**
     * 身份证反面
     */
    @TableField(value = "id_card_back")
    private String idCardBack;


    /**
     * 审核状态:1:待上传，2：未审核，3：已通过，4：未通过
     */
    @TableField(value = "audit_status")
    private Integer auditStatus;

    /**
     * 品牌故事/商家描述(富文本)
     */
    @TableField(value = "merchant_desc")
    private String merchantDesc;

    /**
     * 商户状态：1-启用 2- 禁用
     */
     @TableField(value = "merchant_status")
    private Integer merchantStatus;
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