package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsExtendDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsSkuDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lhm
 * @date 2020/7/5
 * @description
 **/
@Data
@ApiModel("商户详情vo")
public class MerchantGoodsDetailsVO implements Serializable {

    @ApiModelProperty(value = "主键id，修改时传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "商品id",hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty(value = "商家商品编号 修改时传")
    private String merchantSpuCode;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品详情图片")
    private String infoPicture;
    @ApiModelProperty(value = "商品详情图片集合")
    private String[] infoPictureList;
    @ApiModelProperty("商品详细介绍，是富文本格式")
    private String goodsDescribeText;
    @ApiModelProperty("标签id集合")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> labelIdList;
    @ApiModelProperty(value = "标签名集合")
    private List<String> labelNameList;
    @ApiModelProperty("商品拓展")
    private MerchantGoodsExtendDTO merchantGoodsExtendDTO;
    @ApiModelProperty("商品sku集合")
    private List<MerchantGoodsSkuDTO> merchantGoodsSkuDTOList;


    @ApiModelProperty("商品简介--v1.4.0")
    private String goodsSynopsis;

}
