package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/7/7
 * @description
 **/
@Data
public class MerchantGoodsDetailDTO {
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商家商品编号")
    private String spuCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品列表图")
    private String listPicture;

    @ApiModelProperty(value = "商品详情图片")
    private String infoPicture;

    @ApiModelProperty(value = "商品分享图片")
    private String sharePicture;

    @ApiModelProperty(value = "商品详细介绍，是富文本格式")
    private String goodsDescribeText;

    @ApiModelProperty(value = "商户id")
    private Long merchantId;

    @ApiModelProperty(value = "商品简介")
    private String goodsSynopsis;

    @ApiModelProperty(value = "1-下架,2-上架")
    private Integer merchantGoodsStatus;

    @ApiModelProperty(value = "0-否 1-是")
    private Boolean isDel;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;
}
