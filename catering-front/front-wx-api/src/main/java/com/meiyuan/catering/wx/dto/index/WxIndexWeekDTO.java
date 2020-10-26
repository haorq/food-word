package com.meiyuan.catering.wx.dto.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author yaoozu
 * @description 首页每日菜单星期几日期计算
 * @date 2020/3/2115:29
 * @since v1.0.0
 */
@Data
@Builder
@ApiModel("首页菜单日期DTO")
public class WxIndexWeekDTO {
    @ApiModelProperty("星期几")
    private String week;
    @ApiModelProperty("日期")
    private String date;
}
