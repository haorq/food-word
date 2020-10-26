package com.meiyuan.catering.wx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/4 10:19
 */
@Data
public class WxAdvertisingExtVO implements Serializable {
    private static final long serialVersionUID = -2020090917281105001L;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String describeTxt;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
