package com.meiyuan.catering.wx.dto.index;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yaoozu
 * @description 同一地址信息
 * @date 2020/5/1115:21
 * @since v1.0.0
 */
@Data
@Builder
public class WxSameLocationDTO implements Serializable {

    private static final long serialVersionUID = -1032022850777503709L;
    @ApiModelProperty(value = "所在城市编码")
    private String cityCode;
    @ApiModelProperty(value = "位置：经度,纬度")
    private String location;
}
