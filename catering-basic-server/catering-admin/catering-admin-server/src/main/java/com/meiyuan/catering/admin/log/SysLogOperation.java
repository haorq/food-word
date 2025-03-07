package com.meiyuan.catering.admin.log;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 操作日志
 *
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysLogOperation extends BaseLog {
    private static final long serialVersionUID = 1L;
    /**
     * 用户操作
     */
    private String operation;
    /**
     * 请求URI
     */
    private String requestUri;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 请求时长(毫秒)
     */
    private Integer requestTime;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 操作IP
     */
    private String ip;
    /**
     * 状态  0：失败   1：成功
     */
    private Integer status;
    /**
     * 用户名
     */
    private String creatorName;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
