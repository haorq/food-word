package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单评价表(CateringOrdersAppraise)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@TableName("catering_orders_appraise")
public class CateringOrdersAppraiseEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = -18183696017546575L;

    /** 订单编号 */
    private String orderNumber;
    /** 订单表ID */
    private Long orderId;
    /** 商户ID */
    private Long merchantId;
    /** 门店ID */
    private Long storeId;
    /** 门店名称 */
    private String storeName;
    /** 门店图片 */
    private String storePicture;
    /** 用户id */
    private Long userId;
    /** 用户昵称 */
    private String userNickname;
    /** 用户头像 */
    private String userAvatar;
    /** 评论内容 */
    private String content;
    /** 商家回复 */
    private String reply;
    /** 评价状态（1：好评，2：中评，3：差评） */
    private Integer status;
    /** 评论评分(1:差评 2-3:中评 4-5:好评) */
    private BigDecimal appraiseLevel;
    /** 口味评分 */
    private Integer taste;
    /** 服务评分*/
    private Integer service;
    /** 包装评分 */
    private Integer pack;
    /** 评论图片集合 */
    private String appraisePicture;
    /** 评论类型（1：用户评论，2：系统自动评论） */
    private Integer appraiseType;
    /** 浏览次数 */
    private Integer browse;
    /** 是否匿名（0：否[默认]，1：是） */
    @TableField("is_anonymous")
    private Boolean anonymous;
    /** 是否展示（0：展示[默认，1：不展示） */
    @TableField("is_show")
    private Boolean canShow;
    /** 是否删除（0：未删除[默认]；1：已删除） */
    @TableField("is_del")
    private Boolean del;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 评论回复时间 */
    private LocalDateTime replyTime;

}
