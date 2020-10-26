package com.meiyuan.catering.wx.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.es.enums.marketing.MarketingUsingObjectEnum;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author yaoozu
 * @description 用户登录
 * @date 2020/3/2111:30
 * @since v1.0.0
 */
@Data
public class UserTokenDTO implements Serializable {
    private static final long serialVersionUID = 5674577292360916761L;
    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @ApiModelProperty(hidden = true)
    private String token;
    @ApiModelProperty(hidden = true)
    private String openId;
    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userCompanyId;
    @ApiModelProperty(hidden = true)
    private String nickname;
    @ApiModelProperty(hidden = true)
    private String phone;
    @ApiModelProperty(hidden = true)
    private Integer userType;
    @ApiModelProperty(hidden = true)
    private String avatar;
    @ApiModelProperty(hidden = true)
    private Integer gender;
    @ApiModelProperty(hidden = true)
    private Integer userLevel;
    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userIdReal;
    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groundPusherId;


    public Long getUserId(){
        if (Objects.equals(userType, UserTypeEnum.COMPANY.getStatus())){
           return userCompanyId;
        }
        return userId;
    }

    public Long getUserIdReal(){
        return userId;
    }

    public boolean isCompanyUser(){
        return Objects.equals(userType, UserTypeEnum.COMPANY.getStatus());
    }

    public Integer getMarketingUserType() {
        if (UserTypeEnum.PERSONAL.getStatus().equals(userType)) {
            return MarketingUsingObjectEnum.PERSONAL.getStatus();
        } else {
            return MarketingUsingObjectEnum.ENTERPRISE.getStatus();
        }
    }

}
