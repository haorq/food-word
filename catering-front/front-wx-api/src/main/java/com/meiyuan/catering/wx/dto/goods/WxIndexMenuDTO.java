package com.meiyuan.catering.wx.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 微信首页DTO
 * @date 2020/3/2115:05
 * @since v1.0.0
 */
@Data
@ApiModel("首页菜单部分DTO")
public class WxIndexMenuDTO {
    @ApiModelProperty("菜单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("商户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty(value = "菜单图片")
    private String menuPicture;

    @ApiModelProperty(value = "菜单描述")
    private String menuDescribe;
}
