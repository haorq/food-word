package com.meiyuan.catering.admin.log;

import lombok.Data;

import java.io.Serializable;


/**
 * @author admin
 */
@Data
public abstract class BaseLog implements Serializable {
    /**
     * 日志类型
     */
    private Integer type;

}
