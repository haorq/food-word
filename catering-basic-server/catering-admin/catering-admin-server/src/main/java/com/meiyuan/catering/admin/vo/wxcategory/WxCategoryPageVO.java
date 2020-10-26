package com.meiyuan.catering.admin.vo.wxcategory;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/4 14:57
 */
@Data
@ApiModel("小程序类目列表数据")
public class WxCategoryPageVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("类目 1:导航栏 2:推荐区 3:爆品推荐")
    private Integer type;

    @ApiModelProperty("类目中文意思")
    private String typeStr;

    @ApiModelProperty("类目图标")
    private String icon;

    @ApiModelProperty("类目名称")
    private String name;

    @ApiModelProperty("排序号")
    private Integer sort;

    @ApiModelProperty("跳转类型:1:现有地址 2:自定义跳转 3:跳转至第三方地址")
    private Integer linkType;

    @ApiModelProperty("跳转链接")
    private String link;

    @ApiModelProperty("跳转链接中文")
    private String linkStr;

    @ApiModelProperty(value = "状态 1:启用 0:禁用")
    private Integer status;

    @ApiModelProperty(value = "爆款是否存在-存在（true）")
    private Boolean hotMoneyFlag;
}
