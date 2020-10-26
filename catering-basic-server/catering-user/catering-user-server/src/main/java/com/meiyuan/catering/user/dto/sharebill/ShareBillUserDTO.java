package com.meiyuan.catering.user.dto.sharebill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yaoozu
 * @description 拼单人信息
 * @date 2020/3/2511:55
 * @since v1.0.0
 */
@Data
@ApiModel("拼单人信息")
public class ShareBillUserDTO implements Serializable {
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "微信头像")
    private String avatar;
    @ApiModelProperty(value = "微信昵称")
    private String nickname;
}
