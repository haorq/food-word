package com.meiyuan.catering.admin.dto.wxcategory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description：小程序类目二级页面新增/修改
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/3 17:02
 */
@Data
public class WxCategoryExtSaveDTO implements Serializable {
    private static final long serialVersionUID = 202008061657110502L;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String describeTxt;
}
