package com.meiyuan.catering.user.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/4 15:48
 */
@Data
@ApiModel("用户手机号信息")
public class UserSessionInfoVO {

    @ApiModelProperty("微信用户open_id")
    private String openId;

    @ApiModelProperty("微信用户手机号")
    private String phone;
}
