package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商户申请授权商品表(CateringMerchantApplyEmpowerGoods)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:39:36
 */
@Data
@TableName("catering_merchant_apply_empower_goods")
public class CateringMerchantApplyEmpowerGoodsEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = -24230766050326294L;
    /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;
    /**
     * 数量
     */
    @TableField(value = "number")
    private Long number;
    /**
     * 1-未通过 2-通过
     */
    @TableField(value = "audit_status")
    private Integer auditStatus;
    /**
     * 未通过理由
     */
    @TableField(value = "reasons_for_failure")
    private String reasonsForFailure;
    /**
     * 逻辑删除 0-没有删除 1-删除
     */
    @TableField(value = "is_del")
    private Boolean del;
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_by")
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}