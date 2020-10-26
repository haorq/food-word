package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志
 * @author admin
 */
@Data
@TableName("catering_log_operation")
public class CateringLogOperationEntity extends IdEntity implements Serializable {
	/**
	 * 用户操作
	 */
	@TableField("operation")
	private String operation;
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
	 * 请求时长(毫秒)
	 */
	@TableField("request_time")
	private Integer requestTime;
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
	 * 状态  0：失败   1：成功
	 */
	@TableField("status")
	private Integer status;
	/**
	 * 用户名
	 */
	@TableField("creator_name")
	private String creatorName;
	/**
	 * 创建者
	 */
	@TableField("create_by")
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private LocalDateTime createTime;

}
