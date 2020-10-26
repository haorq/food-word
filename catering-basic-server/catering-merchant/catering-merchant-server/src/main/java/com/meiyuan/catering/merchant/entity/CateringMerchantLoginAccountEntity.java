package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Date 2020/7/1 0001 11:50
 * @Description 简单描述 :  商户登陆账号表实体类
 * @Since version-1.0.0
 */
@Data
@TableName("catering_merchant_login_account")
public class CateringMerchantLoginAccountEntity extends IdEntity implements Serializable {

        private static final long serialVersionUID = 5903432458810961828L;

        @ApiModelProperty(value = "登陆电话")
        @TableField("phone")
        private String phone;

        @ApiModelProperty(value = "登录密码")
        @TableField("password")
        private String password;

        @ApiModelProperty(value = "账号类型 ：1：品牌/商家，2：店铺，3：自提点，4：商家兼自提点，5：员工")
        @TableField("account_type")
        private Integer accountType;

        @ApiModelProperty(value = "账号类型对应id")
        @TableField("account_type_id")
        private Long accountTypeId;

        @ApiModelProperty(value = "账户状态:1:启用，2：禁用")
        @TableField("account_status")
        private Integer accountStatus;

        @ApiModelProperty(value = "是否删除：0：否，1：是")
        @TableField(value = "is_del",fill = FieldFill.INSERT)
        private Boolean isDel;

        @ApiModelProperty(value = "最近一次登录时间")
        @TableField("last_login_time")
        private LocalDateTime lastLoginTime;

        /**
         * 创建时间
         */
        @TableField(value = "create_time", fill = FieldFill.INSERT)
        private LocalDateTime createTime;
        /**
         * 修改时间
         */
        @TableField(value = "update_time", fill = FieldFill.UPDATE)
        private LocalDateTime updateTime;
}







