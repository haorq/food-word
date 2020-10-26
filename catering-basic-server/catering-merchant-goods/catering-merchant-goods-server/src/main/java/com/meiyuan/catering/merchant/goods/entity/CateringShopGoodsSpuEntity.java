package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Data
@TableName("catering_shop_goods_spu")
public class CateringShopGoodsSpuEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = -4634906377222585640L;

    @TableField("spu_code")
    private String spuCode;


    @TableField("goods_id")
    private Long goodsId;

    @TableField("merchant_id")
    private Long merchantId;

    @TableField("shop_id")
    private Long shopId;

    @TableField("shop_goods_status")
    private Integer shopGoodsStatus;

    @TableField("sort")
    private Integer sort;

    @TableField("goods_and_type")
    private Integer goodsAndType;

    @TableField("is_del")
    @TableLogic
    private Boolean del;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
