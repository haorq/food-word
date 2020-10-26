package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表(CateringUser)实体类
 *
 * @author mei-tao
 * @since 2020-03-10 15:28:45
 */
@Data
@TableName("catering_user")
public class CateringUserEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = -53072334232475529L;
    /** 企业id */
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Long userCompanyId;
    /** 用户来源：0:自然流量 1:地推 2:被邀请*/
    private Integer pullNewUser;
    /** 姓名 */
    private String userName;
    /** 1：男；2-女；3-其他 */
    private Integer gender;
    /** 电话 */
    private String phone;
    /** 电话 */
    private String firstPhone;
    /** 注册IP */
    private String registerIp;
    /** 微信用户open_id */
    private String openId;
    /** 微信用户union_id */
    private String unionId;
    /** 微信头像 */
    private String avatar;
    /** 微信昵称 */
    private String nickname;
    /** 地区 */
    private String area;
    /** 地推员id */
    private Long groundPusherId;
    /** saas用户id */
    private Long saasUserId;
    /** saas用户编码 */
    private String saasUserCode;
    /** saas商户id */
    private Long saasMerchantCode;

    /** 1：企业用户，2：个人用户 */
    private Integer userType;
    /** 1：正常；2：注销 */
    private Integer status;
    /** 1：普通用户；2：会员卡用户；3：其他 */
    private Integer userLevel;
    /** 注册时间 */
    private LocalDateTime registerTime;
    /** 0：正常；1：删除 */
    @TableField("is_del")
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
