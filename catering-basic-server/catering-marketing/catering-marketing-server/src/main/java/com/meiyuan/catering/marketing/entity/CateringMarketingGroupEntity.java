package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 营销拼团(CateringMarketingGroup)实体类
 *
 * @author wxf
 * @since 2020-03-10 11:21:41
 */
@Data
@TableName("catering_marketing_group")
public class CateringMarketingGroupEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = -18738175712197175L;
     /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
     /**
     * 类型：按照人数、按照商品数量
     */
    @TableField(value = "group_type")
    private Integer groupType;
     /**
     * 编码
     */
    @TableField(value = "group_code")
    private String groupCode;
     /**
     * 名称
     */
    @TableField(value = "group_name")
    private String groupName;
     /**
     * 说明
     */
    @TableField(value = "group_desc")
    private String groupDesc;
     /**
     * 金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;
     /**
     * 优惠金额
     */
    @TableField(value = "discount_amount")
    private BigDecimal discountAmount;
     /**
     * 发行数量： -1 - 不限制
     */
    @TableField(value = "publish_quantity")
    private Integer publishQuantity;
     /**
     * 用户限购： -1 - 不限制
     */
    @TableField(value = "limit_quantity")
    private Integer limitQuantity;
     /**
     * 开始时间
     */
    @TableField(value = "begin_time")
    private LocalDateTime beginTime;
     /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;
     /**
     * 成团有效时间
     */
    @TableField(value = "valid_time")
    private Long validTime;
     /**
     * 是否允许单独购买
     */
    @TableField(value = "alone_buy")
    private Integer aloneBuy;
     /**
     * 成团人数
     */
    @TableField(value = "group_member")
    private Integer groupMember;
     /**
     * 活动对象：个人、企业
     */
    @TableField(value = "object_limit")
    private Integer objectLimit;
     /**
     * 最大虚拟成团人数
     */
    @TableField(value = "max_virtual_member")
    private Integer maxVirtualMember;
     /**
     * 团长优惠
     */
    @TableField(value = "sponsor_discount")
    private BigDecimal sponsorDiscount;
     /**
     * 上架/下架
     */
    @TableField(value = "up_down")
    private Integer upDown;
     /**
     * 删除标记
     */
    @TableField(value = "is_del")
    private Boolean del;
     /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
     /**
     * 创建人
     */
    @TableField(value = "create_by")
    private Long createBy;
     /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
     /**
     * 更新人
     */
    @TableField(value = "update_by")
    private Long updateBy;
    }