package com.meiyuan.catering.core.dto.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/5/6
 */
@Data
public class RedisNoticeDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id")
    private Long id;
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
     * 逻辑删除
     */
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
