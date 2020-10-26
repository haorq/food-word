package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author MeiTao
 * @Date 2020/10/9 0009 9:51
 * @Description 简单描述 :  店铺扩展信息
 * @Since version-1.5.0
 */
@TableName("catering_shop_ext")
@ApiModel(value="CateringShopExtEntity对象", description="店铺扩展表")
@Data
public class CateringShopExtEntity extends IdEntity implements Serializable {
        @ApiModelProperty(value = "门店id")
        @TableField("shop_id")
        private Long shopId;

        @ApiModelProperty(value = "与通联绑定合同号")
        @TableField("contract_no")
        private String contractNo;

        @ApiModelProperty(value = "通联实名认证绑定手机号")
        @TableField("tl_phone")
        private String tlPhone;

        @ApiModelProperty(value = "门店与通联签约状态： 1：签约，2：未签约")
        @TableField("sign_status")
        private Integer signStatus;

        @ApiModelProperty(value = "开户银行")
        @TableField("bank_name")
        private String bankName;

        @ApiModelProperty(value = "开户支行")
        @TableField("bank_branch")
        private String bankBranch;

        @ApiModelProperty(value = "开户行卡号")
        @TableField("bank_card_number")
        private String bankCardNumber;

        @ApiModelProperty(value = "营业执照")
        @TableField("business_license")
        private String businessLicense;

        @ApiModelProperty(value = "食品经营许可证")
        @TableField("food_business_license")
        private String foodBusinessLicense;

        @ApiModelProperty(value = "身份证正面")
        @TableField("id_card_positive")
        private String idCardPositive;

        @ApiModelProperty(value = "身份证反面")
        @TableField("id_card_back")
        private String idCardBack;

        @ApiModelProperty(value = "手持身份证")
        @TableField("id_card_withhand")
        private String idCardWithhand;

        @ApiModelProperty(value = "店铺与通联绑定状态：1-实名认证、2-电子签约、3-绑定银行卡、4-未进行任何步骤")
        @TableField("audit_status")
        private Integer auditStatus;

        @ApiModelProperty(value = "是否删除")
        @TableField(value = "is_del",fill = FieldFill.INSERT)
        private Boolean del;

        @ApiModelProperty(value = "创建时间")
        @TableField(value = "create_time",fill = FieldFill.INSERT)
        private LocalDateTime createTime;

        @ApiModelProperty(value = "更新时间")
        @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime updateTime;

        @ApiModelProperty(value = "用户姓名")
        @TableField("user_name")
        private String userName;

        @ApiModelProperty(value = "用户真实身份证号")
        @TableField("id_card")
        private String idCard;

        @ApiModelProperty(value = "银行卡/账户属性: 0-个人银行卡、1-企业对公账户")
        @TableField("bank_card_pro")
        private Long bankCardPro;

}







