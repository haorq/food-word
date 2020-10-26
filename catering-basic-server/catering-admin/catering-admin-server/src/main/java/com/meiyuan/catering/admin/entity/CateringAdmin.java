package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @description 管理员
 * @author yaozou
 * @date 2020/2/25 16:57
 * @since v1.0.0
 */
@Data
@TableName("catering_admin")
public class CateringAdmin extends IdEntity {
	@TableField("id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long id;
	/** 管理员名称 */
	@TableField("username")
	private String username;
	/** 管理员密码 */
	@TableField("password")
	private String password;
	/** 最近一次登录IP地址 */
	@TableField("last_login_ip")
	private String lastLoginIp;
	/** 最近一次登录时间 */
	@TableField("last_login_time")
	private LocalDateTime lastLoginTime;
	/** 头像图片 */
	@TableField("avatar")
	private String avatar;
	/** 创建时间 */
	@TableField("create_time")
	private LocalDateTime createTime;
	/** 更新时间 */
	@TableField("update_time")
	private LocalDateTime updateTime;
	/** 逻辑删除 默认0 */
	@TableField("is_del")
	@TableLogic
	private Boolean isDel;
	/** 角色列表 */
	@TableField("role_ids")
	private Long[] roleIds;

	/** 联系电话 */
	@TableField("phone")
	private String phone;
	/** 1正常 2禁用 */
	@TableField("status")
	private Integer status;
	/**
	 * 创建者
	 */
	@TableField("create_by")
	private Long createBy;
	/**
	 * 创建者
	 */
	@TableField("update_by")
	private Long updateBy;



}
