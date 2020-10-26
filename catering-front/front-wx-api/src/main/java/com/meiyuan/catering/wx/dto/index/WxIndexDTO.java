package com.meiyuan.catering.wx.dto.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 首页
 * @date 2020/3/2116:00
 * @since v1.0.0
 */
@Data
@ApiModel("首页DTO")
public class WxIndexDTO {
    @ApiModelProperty(value = "位置：经度,纬度")
    private String location;

    @ApiModelProperty("地址简称")
    private String locationName;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("城市编码")
    private String cityCode;

    @ApiModelProperty(value = "扫描地推员二维码提示信息")
    private String pusherMsg;
    @ApiModelProperty(value = "城市店铺总数")
    private Long cityShopSum;


    public WxIndexDTO() {
    }
}
