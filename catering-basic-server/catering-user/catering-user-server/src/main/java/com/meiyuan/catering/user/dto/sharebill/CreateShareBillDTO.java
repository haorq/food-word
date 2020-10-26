package com.meiyuan.catering.user.dto.sharebill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 创建拼单
 * @date 2020/3/2711:58
 * @since v1.0.0
 */
@Data
@ApiModel("创建拼单")
public class CreateShareBillDTO {
    @ApiModelProperty(value = "用户ID", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shareUserId;

    @ApiModelProperty(value = "微信头像", hidden = true)
    private String avatar;

    @ApiModelProperty(value = "微信昵称", hidden = true)
    private String nickname;

    @ApiModelProperty(value = "商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "菜单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
    @ApiModelProperty(value = "拼单类别:1--菜单，2--商品")
    private Integer type;
}
