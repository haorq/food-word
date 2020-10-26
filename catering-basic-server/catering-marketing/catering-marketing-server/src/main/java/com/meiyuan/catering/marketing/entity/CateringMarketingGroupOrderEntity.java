package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 团单数据表(CateringMarketingGroupOrder)实体类
 *
 * @author wxf
 * @since 2020-03-10 11:21:41
 */
@Data
@TableName("catering_marketing_group_order")
public class CateringMarketingGroupOrderEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 318251478086563877L;
    /**
     * 关联ID
     */
    @TableField(value = "of_id")
    private Long ofId;
    /**
     * 关联ID归属类型:1-秒杀；2-拼团；3-团购；4-优惠券；
     */
    @TableField(value = "of_type")
    private Integer ofType;
    /**
     * 营销商品表主键id
     */
    @TableField(value = "m_goods_id")
    private Long mGoodsId;
    /**
     * 发起拼团时间(订单支付成功的时间)
     */
    @TableField(value = "group_start_time")
    private LocalDateTime groupStartTime;
    /**
     * 拼团结束时间
     */
    @TableField(value = "group_end_time")
    private LocalDateTime groupEndTime;
    /**
     * 成团有效时间
     */
    @TableField(value = "group_valid_time")
    private Long groupValidTime;
    /**
     * 成团人数
     */
    @TableField(value = "group_member")
    private Integer groupMember;
    /**
     * 已参团人数
     */
    @TableField(value = "now_member")
    private Integer nowMember;
    /**
     * 商品成团数量
     */
    @TableField(value = "group_goods_number")
    private Integer groupGoodsNumber;
    /**
     * 商品已拼数量
     */
    @TableField(value = "now_goods_number")
    private Integer nowGoodsNumber;
    /**
     * 状态：1-拼团中；2-拼团成功；3-拼团失败
     */
    @TableField(value = "status")
    private Integer status;
    @TableField(value = "is_del")
    @TableLogic
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