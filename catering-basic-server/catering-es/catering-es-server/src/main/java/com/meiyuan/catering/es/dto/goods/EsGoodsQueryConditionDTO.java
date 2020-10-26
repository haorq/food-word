package com.meiyuan.catering.es.dto.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/7/18 0018 10:18
 * @Description 简单描述 : 商品es查询条件
 * @Since version-1.2.0
 */
@Data
public class EsGoodsQueryConditionDTO {
    @ApiModelProperty("城市编码")
    private String cityCode;
    @ApiModelProperty("商品id")
    private Long goodsId;
    @ApiModelProperty("商品id集合")
    private List<Long> goodsIdList;
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
    @ApiModelProperty("skuCode")
    private String skuCode;
    @ApiModelProperty("分类id")
    private Long categoryOrMenuId;
    @ApiModelProperty("门店上下架 1-下架,2-上架")
    private Integer goodsStatus;
    @ApiModelProperty("商户上下架 1-下架,2-上架")
    private Integer merchantGoodsStatus;
    @ApiModelProperty("关键字")
    private String name;
    @ApiModelProperty("首页分类id")
    private String wxIndexCategoryId;
    @ApiModelProperty(value = "售卖状态：1-进行中；2-未开始；3-已结束",hidden = true)
    private Integer sellStatus;
    @ApiModelProperty("或者查询-商品名称")
    private String shouldGoodsName;
    @ApiModelProperty("商品售卖渠道查询1.外卖小程序 2:食堂 3.全部")
    private Integer salesChannels;
    @ApiModelProperty("是否包含推送给商家的商品 true包含（商家） false不包含（平台） null所有（包含平台和商家）")
    private Boolean flag;
}
