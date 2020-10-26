package com.meiyuan.catering.core.dto.base;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/6 14:06
 */
@Data
public class RedisWxCategoryExtDTO implements Serializable {

    private static final long serialVersionUID = 202008061429110601L;

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
