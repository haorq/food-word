package com.meiyuan.catering.core.dto.base;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/4 9:39
 */
@Data
public class RedisAdvertisingExtDTO implements Serializable {

    private static final long serialVersionUID = 202009040940110501L;

    /**
     * 图标
     */
    private String icon;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String describeTxt;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
