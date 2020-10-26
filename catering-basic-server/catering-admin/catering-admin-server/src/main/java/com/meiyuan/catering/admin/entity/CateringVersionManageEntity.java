package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author MeiTao
 * @Date 2020/9/10 0010 13:48
 * @Description 简单描述 : 移动端版本管理
 * @Since version-1.4.0
 */
@TableName("catering_version_manage")
@Data
public class CateringVersionManageEntity extends IdEntity implements Serializable  {
        @ApiModelProperty(value = "app唯一标识")
        @TableField("app_id")
        private String appId;
        @ApiModelProperty(value = "是否强制更新：true:是")
        @TableField("forced_update")
        private Boolean forcedUpdate;

        @ApiModelProperty(value = "是否提醒更新：true:是")
        @TableField("remind_update")
        private Boolean remindUpdate;

        @ApiModelProperty(value = "当前最新版本号")
        @TableField("version_num")
        private String versionNum;

        @ApiModelProperty(value = "版本更新提示语")
        @TableField("remind_word")
        private String remindWord;

        @ApiModelProperty(value = "创建时间")
        @TableField(value = "create_time",fill = FieldFill.INSERT)
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
        private Date updateTime;
}







