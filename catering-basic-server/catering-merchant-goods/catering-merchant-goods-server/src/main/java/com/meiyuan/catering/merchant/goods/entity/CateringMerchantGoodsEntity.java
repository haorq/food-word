package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Data
@TableName("catering_merchant_goods")
public class CateringMerchantGoodsEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 8233655380240090754L;


    /**
     *  商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 商家商品编号
     */
    @TableField("spu_code")
    private String spuCode;
    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;
    /**
     * 商品列表图
     */
    @TableField("list_picture")
    private String listPicture;
    /**
     * 商品详情图片
     */
    @TableField("info_picture")
    private String infoPicture;
    /**
     * 商品分享图片
     */
    @TableField("share_picture")
    private String sharePicture;
    /**
     * 商品详细介绍，是富文本格式
     */
    @TableField("goods_describe_text")
    private String goodsDescribeText;
    /**
     * 商品简介
     */
    @TableField("goods_synopsis")
    private String goodsSynopsis;

    /**
     * 0-否 1-是
     */
    @TableField("is_del")
    @TableLogic
    private Boolean del;
    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField("update_by")
    private Long updateBy;

}
