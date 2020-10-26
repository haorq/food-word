package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

;

/**
 * 赠品表(CateringGoodsGift)实体类
 *
 * @author wxf
 * @since 2020-03-18 18:31:39
 */
@Data
@TableName("catering_goods_gift")
public class CateringGoodsGiftEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = -51083159729413587L;
    /**
     * 赠品编码
     */
    @TableField(value = "gift_code")
    private String giftCode;
     /**
     * 赠品名称
     */
    @TableField(value = "gift_name")
    private String giftName;
     /**
     * 赠品价值
     */
    @TableField(value = "gift_price")
    private BigDecimal giftPrice;
     /**
     * 赠品库存
     */
    @TableField(value = "gift_stock")
    private Long giftStock;
     /**
     * 赠品图片
     */
    @TableField(value = "gift_picture")
    private String giftPicture;
    /**
     * 赠品状态 1-禁用 2-启用
     */
    @TableField(value = "gift_status")
    private String giftStatus;
    /**
     * 0-否 1-是
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
}