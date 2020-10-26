package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志
 * @author admin
 */
@Data
@TableName("catering_log_login")
public class CateringLogLoginEntity extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户操作   0：用户登录   1：用户退出
	 */
	@TableField("operation")
	private Integer operation;
	/**
	 * 状态  0：失败    1：成功    2：账号已锁定
	 */
	@TableField("status")
	private Integer status;
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
	 * 用户名
	 */
	@TableField("creator_name")
	private String creatorName;
	/**
	 * 创建者
	 */
	@TableField("create_by")
	private Long createBy;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;

}
