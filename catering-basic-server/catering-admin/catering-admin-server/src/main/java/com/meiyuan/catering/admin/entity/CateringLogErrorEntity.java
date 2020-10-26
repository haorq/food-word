package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 异常日志
 * @author admin
 */
@Data
@TableName("catering_log_error")
public class CateringLogErrorEntity extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 请求URI
	 */
	@TableField("request_uri")
	private String requestUri;
	/**
	 * 请求方式
	 */
	@TableField("request_method")
	private String requestMethod;
	/**
	 * 请求参数
	 */
	@TableField("request_params")
	private String requestParams;
	/**
	 * 用户代理
	 */
	@TableField("user_agent")
	private String userAgent;
	/**
	 * 操作IP
	 */
	@TableField("ip")
	private String ip;
	/**
	 * 异常信息
	 */
	@TableField("error_info")
	private String errorInfo;
	/**
	 * 创建者
	 */
	@TableField("creator_name")
	private String createBy;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;

}
