package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.goods.dto.goods.GoodsSortDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lhm
 * @date 2020/7/5
 * @description
 **/
@Data
@ApiModel("商户商品新增/修改 dto")
public class MerchantGoodsDTO  {

    @ApiModelProperty("id 新增时传，修改不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "商品id   修改时传")
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
    @ApiModelProperty("商品简介--v1.4.0")
    private String goodsSynopsis;
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("门店id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty("标签id集合")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> labelIdList;
    @ApiModelProperty(value = "标签名集合",hidden = true)
    private List<String> labelNameList;
    @ApiModelProperty("商品拓展")
    private MerchantGoodsExtendDTO merchantGoodsExtendDTO;
    @ApiModelProperty("商品sku集合")
    private List<MerchantGoodsSkuDTO> merchantGoodsSkuDTOList;
    @ApiModelProperty(value = "商品排序",hidden = true)
    private Integer categoryGoodsSort;



    @ApiModelProperty(value = "门店对应的商品排序",hidden = true)
    private List<GoodsSortDTO> sortDTOS;
}
