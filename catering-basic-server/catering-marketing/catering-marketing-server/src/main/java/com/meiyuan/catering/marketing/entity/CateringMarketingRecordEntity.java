package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购买记录表(CateringMarketingRecord)实体类
 *
 * @author wxf
 * @since 2020-03-10 11:21:41
 */
@Data
@TableName("catering_marketing_record")
public class CateringMarketingRecordEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = 628055207504066861L;
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
     * 活动商品表主键id
     */
    @TableField(value = "m_goods_id")
    private Long mGoodsId;
     /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;
     /**
     * 分类统计单个用户的购买数量
     */
    @TableField(value = "user_count")
    private Integer userCount;
     /**
     * 删除标记
     */
    @TableField(value = "is_del")
    private Boolean del;
     /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
     /**
     * 创建人
     */
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;
     /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
     /**
     * 更新人
     */
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    }