package com.meiyuan.catering.wx.dto.sharebill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 购物车拼单人信息
 * @date 2020/3/2511:55
 * @since v1.0.0
 */
@Data
@ApiModel("购物车拼单人信息")
public class WxCartShareBillUserDTO {
    @ApiModelProperty("拼单号")
    private String shareBillNo;
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("微信头像")
    private String avatar;
    @ApiModelProperty("微信昵称")
    private String nickname;
}
