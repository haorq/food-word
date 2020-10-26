package com.meiyuan.catering.es.dto.goods;

import com.meiyuan.catering.es.dto.property.EsGoodsPropertyDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantBaseInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/26 11:05
 * @description 简单描述
 **/
@Data
@ApiModel("ES商品索引DTO")
public class EsGoodsIndexDTO extends EsMerchantBaseInfoDTO {
    /**
     * 这个字段 团购/秒杀/用户购物车 判断标识
     */
    @ApiModelProperty(hidden = true, value = "新增 true 反则 修改")
    private Boolean flag;
    /**
     * es 唯一id
     */
    @ApiModelProperty("唯一id")
    private String id;
    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private String goodsId;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String goodsName;
    /**
     * 菜品编号
     */
    @ApiModelProperty("菜品编号")
    private String spuCode;
    /**
     * 菜品分类id
     */
    @ApiModelProperty("菜品分类id")
    private String categoryId;
    /**
     * 菜品分类名称
     */
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    /**
     * 列表图片
     */
    @ApiModelProperty("列表图片")
    private String listPicture;
    /**
     * 详情图片
     */
    @ApiModelProperty("详情图片")
    private String infoPicture;
    /**
     * 平台上下架 1-下架,2-上架
     */
    @ApiModelProperty("平台上下架 1-下架,2-上架")
    private Integer goodsStatus;
    /**
     * 商户上下架 1-下架,2-上架
     */
    @ApiModelProperty("商户上下架 1-下架,2-上架")
    private Integer merchantGoodsStatus;
    /**
     * 原价(市场价)
     */
    @ApiModelProperty("原价(市场价)")
    private BigDecimal marketPrice;
    /**
     * 销售价
     */
    @ApiModelProperty("销售价")
    private BigDecimal salesPrice;
    /**
     * 企业价
     */
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;
    /**
     * 商品详细介绍
     */
    @ApiModelProperty("商品详细介绍")
    private String goodsDescribeText;
    /**
     * 商品规格类型 1-统一规格 2-多规格
     */
    @ApiModelProperty("商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 商户商品标识 true-是推送给商家的商品 false-单纯的商品信息
     */
    @ApiModelProperty("商户商品标识 true-是推送给商家的商品 false-单纯的商品信息")
    private Boolean merchantGoodsFlag;
    /**
     * 标签集合
     */
    @ApiModelProperty("标签集合")
    private List<EsGoodsCategoryAndLabelDTO> labelList;
    /**
     * 规格集合
     */
    @ApiModelProperty("规格集合")
    private List<EsGoodsSkuDTO> skuList;
    /**
     * 属性集合
     */
    @ApiModelProperty("属性集合")
    private List<EsGoodsPropertyDTO> propertyList;
//    /**
//     * 小程序首页分类对应的商品集合
//     */
//    @ApiModelProperty("小程序首页分类对应的商品集合")
//    private List<EsIndexCategoryGoodsRelationDTO> indexCategoryGoodsList;
}
