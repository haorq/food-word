package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.meiyuan.catering.core.entity.IdEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author zengzhangni
 * @date 2020-05-06
 */
@Data
@TableName("catering_notice")
public class CateringNoticeEntity extends IdEntity {

    /**
     * 公告标题
     */
    private String name;
    /**
     * 内容
     */
    private String content;
    /**
     * 公告位置：1:小程序端首页
     */
    private Integer position;
    /**
     * 是否置顶
     */
    private Boolean isStick;
    /**
     * 发布状态 0:已发布 1:待发布
     */
    private Integer status;
    /**
     * 浏览量
     */
    private Integer pageView;
    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField("is_del")
    private Boolean del;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
