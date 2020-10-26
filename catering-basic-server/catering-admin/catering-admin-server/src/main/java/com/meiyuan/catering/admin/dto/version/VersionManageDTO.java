package com.meiyuan.catering.admin.dto.version;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/9/10 0010 13:48
 * @Description 简单描述 : 移动端版本管理
 * @Since version-1.4.0
 */
@Data
public class VersionManageDTO extends IdEntity{
        @ApiModelProperty(value = "app唯一标识")
        private String appId;
        @ApiModelProperty(value = "是否强制更新：true:是")
        private Boolean forcedUpdate;

        @ApiModelProperty(value = "是否提醒更新：true:是")
        private Boolean remindUpdate;

        @ApiModelProperty(value = "当前最新版本号")
        private String versionNum;

        @ApiModelProperty(value = "版本更新提示语")
        private String remindWord;
}







