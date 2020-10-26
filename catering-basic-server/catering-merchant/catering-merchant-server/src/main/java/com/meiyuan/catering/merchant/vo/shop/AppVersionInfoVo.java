package com.meiyuan.catering.merchant.vo.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author mt
 * @date 2020/3/16 15:10
 **/
@ApiModel("app版本更新信息vo")
@Data
public class AppVersionInfoVo {
    @ApiModelProperty("是否强制更新：true:是")
    private Boolean forcedUpdate;
    @ApiModelProperty("是否提醒更新：true:是")
    private Boolean remindUpdate;
    @ApiModelProperty("当前最新版本号")
    private String versionNum;
    @ApiModelProperty("提示语")
    private List<String> remindWords;
}
