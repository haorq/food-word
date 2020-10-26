package com.meiyuan.catering.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/4 16:41
 */
@Data
@ApiModel("新用户保存信息")
public class UserSaveDTO {

    @ApiModelProperty(hidden = true, value = "微信昵称")
    private String nickName;

    @ApiModelProperty(hidden = true, value = "微信头像")
    private String avatar;

    @ApiModelProperty(hidden = true, value = "国家")
    private String country;

    @ApiModelProperty(hidden = true, value = "省")
    private String province;

    @ApiModelProperty(hidden = true, value = "市")
    private String city;

    @ApiModelProperty(hidden = true, value = "1：男；2-女；3-其他")
    private Integer gender;

    @ApiModelProperty(hidden = true, value = "当前推荐人用户类型")
    private Integer userType;

    @ApiModelProperty(hidden = true, value = "微信用户union_id")
    private String unionId;

    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @ApiModelProperty(value = "微信用户openId", required = true)
    private String openId;

    @ApiModelProperty(value = "加密信息", required = true)
    private String encryptedData;

    @ApiModelProperty(value = "iv", required = true)
    private String iv;

    @ApiModelProperty("推荐人id（推荐有奖时才需要）")
    private Long referrerId;

    @ApiModelProperty(value = "推荐人用户类型")
    private Integer referrerType;

    @ApiModelProperty("地推员id")
    private Long groundPusherId;

    @ApiModelProperty("分享标识-有值就是分享")
    private String shareFlag;
}
