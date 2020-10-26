package com.meiyuan.catering.wx.dto.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 首页请求参数
 * @date 2020/3/2112:07
 * @since v1.0.0
 */
@Data
@ApiModel("首页请求DTO")
public class WxIndexRequestDTO {

    @ApiModelProperty(value = "位置：经度,纬度")
    private String location;

    @ApiModelProperty(value = "位置标识[首页接口返回]：用于获取缓存中的商户列表")
    private String locationFlag;

    @ApiModelProperty(value = "时间：进首页不传，以系统时间为准")
    private String date;

    @ApiModelProperty(value = "小程序类目数量限制（默认为1条）")
    private Integer categoryLimit;

    public WxIndexRequestDTO() {
        this.categoryLimit = 1;
    }
}
