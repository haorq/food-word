package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品规格(SKU)表(CateringGoodsSku)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:37:53
 */
@Data
@TableName("catering_goods_sku")
public class CateringGoodsSkuEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 591861755046271931L;
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
     * sku编码
     */
    @TableField(value = "sku_code")
    private String skuCode;
    /**
     * 规格值
     */
    @TableField(value = "property_value")
    private String propertyValue;
    /**
     * 规格图片
     */
    @TableField(value = "pic_url")
    private String picUrl;
    /**
     * 市场价
     */
    @TableField(value = "market_price")
    private BigDecimal marketPrice;
    /**
     * 销售价
     */
    @TableField(value = "sales_price")
    private BigDecimal salesPrice;
    /**
     * 企业价
     */
    @TableField(value = "enterprise_price")
    private BigDecimal enterprisePrice;
    /**
     * 打包费
     */
    @TableField(value = "pack_price")
    private BigDecimal packPrice;
    /**
     * 库存
     */
    @TableField(value = "stock")
    private Long stock;
    /**
     * 商品规格类型 1-统一规格 2-多规格
     */
    @TableField(value = "goods_spec_type")
    private Integer goodsSpecType;
    /**
     * 最低购买
     */
    @TableField(value = "lowest_buy")
    private Integer lowestBuy;
    /**
     * 最多购买
     */
    @TableField(value = "highest_buy")
    private Integer highestBuy;
    /**
     * 逻辑删除 0-没有删除 1-删除
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    @TableLogic
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