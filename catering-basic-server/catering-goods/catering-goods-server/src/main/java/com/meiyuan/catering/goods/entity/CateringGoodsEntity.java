package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品基本信息(SPU)表(CateringGoods)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:35:00
 */
@Data
@TableName("catering_goods")
public class CateringGoodsEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 112533856650454426L;
    /**
     * 自动编号 true-自动 false-手动
     */
    @TableField(value = "auto_code")
    private Boolean autoCode;
    /**
     * 商品编号
     */
    @TableField(value = "spu_code")
    private String spuCode;
    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    private String goodsName;
    /**
     * 商品列表图
     */
    @TableField(value = "list_picture")
    private String listPicture;
    /**
     * 商品详情图片
     */
    @TableField(value = "info_picture")
    private String infoPicture;
    /**
     * 商品分享图片
     */
    @TableField(value = "share_picture")
    private String sharePicture;
    /**
     * 商品简介
     */
    @TableField(value = "goods_synopsis")
    private String goodsSynopsis;
    /**
     * 1-下架,2-上架
     */
    @TableField(value = "goods_status")
    private Integer goodsStatus;
    /**
     * 商品权重
     */
    @TableField(value = "goods_weight")
    private Long goodsWeight;
    /**
     * 商品单位，例如件、盒( 天，月，年)
     */
    @TableField(value = "unit")
    private String unit;
    /**
     * 商品规格类型 1-统一规格 2-多规格
     */
    @TableField(value = "goods_spec_type")
    private Integer goodsSpecType;
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
     * 商品详细介绍，是富文本格式
     */
    @TableField(value = "goods_describe_text")
    private String goodsDescribeText;
    /**
     * 最低购买
     */
    @TableField(value = "lowest_buy")
    private Long lowestBuy;
    /**
     * 最多购买
     */
    @TableField(value = "highest_buy")
    private Long highestBuy;

    /**
     * 1-平台 2-店铺
     */
    @TableField(value = "goods_add_type")
    private Integer goodsAddType;
    /**
     * 商户id
     */
    @TableField(value = "merchant_id" , fill = FieldFill.INSERT)
    private Long merchantId;
    /**
     * 开始售卖时间
     */
    @TableField(value = "start_sell_time", updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime startSellTime;
    /**
     * 结束售卖时间
     */
    @TableField(value = "end_sell_time", updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime endSellTime;
    /**
     * 0-否 1-是
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
