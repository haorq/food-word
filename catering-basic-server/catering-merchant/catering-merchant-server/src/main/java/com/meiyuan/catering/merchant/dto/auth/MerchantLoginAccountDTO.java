package com.meiyuan.catering.merchant.dto.auth;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Date 2020/7/1 0001 11:50
 * @Description 简单描述 :  商户登陆账号实体类对DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("商户登陆账号实体类对DTO")
public class MerchantLoginAccountDTO extends IdEntity implements Serializable {

        private static final long serialVersionUID = 5903432458810961828L;

        @ApiModelProperty(value = "登陆电话")
        private String phone;

        @ApiModelProperty(value = "登录密码")
        private String password;

        @ApiModelProperty(value = "账号类型 ：1：品牌/商家，2：店铺，3：自提点，4：商家兼自提点，5：员工")
        private Integer accountType;

        @ApiModelProperty(value = "账号类型对应id")
        private Long accountTypeId;

        @ApiModelProperty(value = "员工id【仅员工登陆时存在】")
        private Long employeeId;

        @ApiModelProperty(value = "账户状态:1:启用，2：禁用")
        private Integer accountStatus;

        @ApiModelProperty(value = "是否删除：0：否，1：是")
        private Boolean isDel;

        @ApiModelProperty(value = "最近一次登录时间")
        private LocalDateTime lastLoginTime;

        @ApiModelProperty(value = "创建时间")
        private LocalDateTime createTime;

        @ApiModelProperty(value = "更新时间")
        private LocalDateTime updateTime;
}







