package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品综合数据表(CateringGoodsData)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:36:32
 */
@Data
@TableName("catering_goods_data")
public class CateringGoodsDataEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 531409339925507497L;
    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;
    /**
     * 商品编号
     */
    @TableField(value = "spu_code")
    private String spuCode;
    /**
     * 点击量
     */
    @TableField(value = "click_count")
    private Long clickCount;
    /**
     * 展示次数
     */
    @TableField(value = "exhibition_count")
    private Long exhibitionCount;
    /**
     * 分享次数
     */
    @TableField(value = "share_count")
    private Long shareCount;
    /**
     * 销量
     */
    @TableField(value = "sales_count")
    private Long salesCount;
    /**
     * 评论量
     */
    @TableField(value = "comment_count")
    private Long commentCount;
    /**
     * 收藏次数
     */
    @TableField(value = "ollectionc_count")
    private Long ollectioncCount;
    /**
     * 起始时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;
    /**
     * 逻辑删除 0-没有删除 1-删除
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    private Boolean del;
    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    public CateringGoodsDataEntity(Long goodsId, Long clickCount, Long exhibitionCount, Long shareCount, Long salesCount, Long commentCount, Long ollectioncCount, LocalDateTime startTime) {
        this.goodsId = goodsId;
        this.clickCount = clickCount;
        this.exhibitionCount = exhibitionCount;
        this.shareCount = shareCount;
        this.salesCount = salesCount;
        this.commentCount = commentCount;
        this.ollectioncCount = ollectioncCount;
        this.startTime = startTime;
    }

    public CateringGoodsDataEntity() {

    }
}