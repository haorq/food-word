package com.meiyuan.catering.es.dto.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/6/2 9:52
 * @description es商品相关查询通用queryBuilder字段参数类
 **/
@Data
public class EsGoodsCommonQueryBuilderDTO {
    @ApiModelProperty("商品id")
    private Long goodsId;
    @ApiModelProperty("商品id集合")
    private List<Long> goodsIdList;
    @ApiModelProperty("skuCode")
    private String skuCode;
    @ApiModelProperty("门店上下架 1-下架,2-上架")
    private Integer goodsStatus;
    @ApiModelProperty("商户上下架 1-下架,2-上架")
    private Integer merchantGoodsStatus;
    @ApiModelProperty(value = "售卖状态：1-进行中；2-未开始；3-已结束",hidden = true)
    private Integer sellStatus;
    @ApiModelProperty("或者查询-商品名称")
    private String shouldGoodsName;
    @ApiModelProperty("商品售卖渠道查询1.外卖小程序 2:食堂 3.全部")
    private Integer salesChannels;


    @ApiModelProperty("商户商品标识 true-是推送给门店的商品 false-单纯的商品信息")
    private Boolean merchantGoodsFlag;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("商户id集合")
    private List<Long> merchantIdList;
    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("门店id集合")
    private List<Long> shopIdList;
    @ApiModelProperty("商品推送门店对应城市编码")
    private String cityCode;


    @ApiModelProperty("分类id")
    private Long categoryOrMenuId;
    @ApiModelProperty("首页分类id")
    private String wxIndexCategoryId;
    @ApiModelProperty("关键字")
    private String name;

}
