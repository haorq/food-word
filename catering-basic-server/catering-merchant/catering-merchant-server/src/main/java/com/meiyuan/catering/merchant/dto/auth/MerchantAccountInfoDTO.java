package com.meiyuan.catering.merchant.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/7/1 0001 11:50
 * @Description 简单描述 :  商户登陆账号基本信息DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("商户登陆账号基本信息DTO")
public class MerchantAccountInfoDTO extends IdEntity implements Serializable {

        private static final long serialVersionUID = 5903432458810961828L;

        @ApiModelProperty(value = "登陆电话")
        private String phone;

        @ApiModelProperty(value = "账号类型 ：1：品牌/商家，2：店铺，3：自提点，4：商家兼自提点，5：员工")
        private Integer accountType;

        @ApiModelProperty(value = "账号类型对应id")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long accountTypeId;

        @ApiModelProperty(value = "仅员工登录时存在【1.5.0】",hidden = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long employeeId;

        @ApiModelProperty(value = "账户状态:1:启用，2：禁用")
        private Integer accountStatus;

        @ApiModelProperty(value = "账户状态是否发生改变 : true:是，false：否【1.3.0】")
        private Boolean accountChange;

        @ApiModelProperty(value = "最近一次登录时间")
        private LocalDateTime lastLoginTime;

        @ApiModelProperty(value = "账号权限 : merchant,shop,pickup,shopAndPickup")
        private List<String> perms;


}







