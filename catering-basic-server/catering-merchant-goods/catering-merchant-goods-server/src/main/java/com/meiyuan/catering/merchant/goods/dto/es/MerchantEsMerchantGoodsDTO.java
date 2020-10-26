package com.meiyuan.catering.merchant.goods.dto.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsSkuDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/23 11:49
 * @description 简单描述
 **/
@Data
@ApiModel("商品模型")
public class MerchantEsMerchantGoodsDTO {
    /**
     * 这个字段 团购/秒杀/用户购物车 判断标识
     */
    @ApiModelProperty(hidden = true, value = "新增 true 反则 修改")
    private Boolean flag;
    @ApiModelProperty("id")
    private Long id;
    private Long goodsId;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("名称")
    private String goodsName;
    @ApiModelProperty("菜品编号")
    private String spuCode;
    @ApiModelProperty("菜品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("分类商品排序号")
    private Integer categoryGoodsSort;
    @ApiModelProperty("列表图片")
    private String listPicture;
    @ApiModelProperty("详情图片")
    private String infoPicture;
    @ApiModelProperty("上下架 1-下架,2-上架")
    private Integer goodsStatus;
    @ApiModelProperty("原价(市场价)")
    private Double marketPrice;
    @ApiModelProperty("销售价")
    private Double salesPrice;
    @ApiModelProperty("企业价")
    private Double enterprisePrice;
    @ApiModelProperty("商品详细介绍")
    private String goodsDescribeText;
    @ApiModelProperty("商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("销量")
    private Long salesCount;
    @ApiModelProperty("月销量")
    private Long monthSalesCount;
    @ApiModelProperty("开始售卖时间")
    private Date startSellTime;
    @ApiModelProperty("结束售卖时间")
    private Date endSellTime;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("商户商品标识 true-是推送给商家的商品 false-单纯的商品信息")
    private Boolean merchantGoodsFlag;
    @ApiModelProperty(hidden = true, value = "false-没有删除 true-删除")
    private Boolean del;

    @ApiModelProperty("规格集合")
    private List<MerchantGoodsSkuDTO> skuList;

}
