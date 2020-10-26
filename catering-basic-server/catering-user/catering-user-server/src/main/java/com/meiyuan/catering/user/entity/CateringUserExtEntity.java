package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户扩展表(CateringUserExt)实体类
 *
 * @author mei-tao
 * @since 2020-03-10 15:28:45
 */
@Data
@TableName("catering_user_ext")
public class CateringUserExtEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = 396702724589148060L;

    /** 用户id */
    private Long userId;
    /** 微信ID */
    private String wechat;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String headImg;
    /** 用户地址 */
    private String address;
    /** 生日 */
    private LocalDateTime birthday;
    /** 职业 */
    private String profession;
    /** 0：正常；1：删除 */
    private Boolean del;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private Long createBy;
    /** 更新人 */
    private Long updateBy;

}
