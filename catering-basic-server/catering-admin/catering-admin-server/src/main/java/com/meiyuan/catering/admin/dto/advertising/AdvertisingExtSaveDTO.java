package com.meiyuan.catering.admin.dto.advertising;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description：广告二级页面上传参数
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/2 15:58
 */
@Data
public class AdvertisingExtSaveDTO implements Serializable {
    private static final long serialVersionUID = 202009021559110502L;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String describeTxt;
}
