package com.meiyuan.catering.es.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/8/6 0006 16:56
 * @Description 简单描述 : 好物推荐二级页面扩展信息Vo
 * @Since version-1.3.0
 */
@Data
@ApiModel("好物推荐二级页面扩展信息Vo")
public class WxGoodCategoryExtVo {
    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String describeTxt;
}
