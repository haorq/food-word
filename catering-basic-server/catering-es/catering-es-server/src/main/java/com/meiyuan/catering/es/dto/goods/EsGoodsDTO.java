package com.meiyuan.catering.es.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.dto.merchant.EsMerchantBaseInfoDTO;
import com.meiyuan.catering.es.dto.property.EsGoodsPropertyDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.es.dto.wx.EsIndexCategoryGoodsRelationDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/23 11:49
 * @description 简单描述
 **/
@Data
@ApiModel("商品模型")
@Accessors(chain = true)
public class EsGoodsDTO extends EsMerchantBaseInfoDTO {
    /**
     * 这个字段 团购/秒杀/用户购物车 判断标识
     */
    @ApiModelProperty(hidden = true, value = "新增 true 反则 修改")
    private Boolean flag;
    @ApiModelProperty(hidden = true, value = "是否为商品排序")
    private Boolean isGoodsSort;
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("商品id")
    private String goodsId;
    @ApiModelProperty("名称")
    private String goodsName;
    @ApiModelProperty("规格值")
    private String propertyValue;
    @ApiModelProperty("菜品编号")
    private String spuCode;
    @ApiModelProperty("菜品分类id")
    private String categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("分类商品排序号")
    private Integer categoryGoodsSort;
    @ApiModelProperty("菜单ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
    @ApiModelProperty("列表图片")
    private String listPicture;
    @ApiModelProperty("详情图片")
    private String infoPicture;
    @ApiModelProperty("门店上下架 1-下架,2-上架")
    private Integer goodsStatus;
    @ApiModelProperty("商户上下架 1-下架,2-上架")
    private Integer merchantGoodsStatus;
    @ApiModelProperty("原价(市场价)")
    private BigDecimal marketPrice;
    @ApiModelProperty("销售价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;
    @ApiModelProperty("包装费")
    private BigDecimal packPrice;
    @ApiModelProperty("商品详细介绍")
    private String goodsDescribeText;
    @ApiModelProperty("商品简介")
    private String goodsSynopsis;
    @ApiModelProperty("最低购买")
    private Integer lowestBuy;
    @ApiModelProperty(value = "V1.4.0 特价商品活动中的特价商品起售数量")
    private Integer minQuantity;
    @ApiModelProperty("最多购买 -1无限制")
    private Integer highestBuy;
    @ApiModelProperty("商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("销量")
    private Integer salesCount;
    @ApiModelProperty("月销量")
    private Integer monthSalesCount;
    @ApiModelProperty("开始售卖时间")

    private Date startSellTime;
    @ApiModelProperty("结束售卖时间")

    private Date endSellTime;
    @ApiModelProperty("是否为预售")
    private Boolean presellFlag;
    @ApiModelProperty(value = "星期一到天（1-7）")
    private String sellWeekTime;
    @ApiModelProperty(value = "当天截止售卖时间（hh:ss）")
    private String closeSellTime;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("商户商品标识 true-是推送给商家的商品 false-单纯的商品信息")
    private Boolean merchantGoodsFlag;
    @ApiModelProperty(hidden = true, value = "false-没有删除 true-删除")
    private Boolean del;
    @ApiModelProperty(hidden = true, value = "根据sku分组使用的字段")
    private String skuCode;
    @ApiModelProperty("购物车选择数量")
    private Integer selectedNum;
    @ApiModelProperty("商品对应商户数量状态 1-一个商家 2-多个商家 3-没有商家")
    private Integer onlyOneStatus;

    @ApiModelProperty("标签集合（数组）")
    private String goodsLabel;


    @ApiModelProperty("标签集合")
    private List<EsGoodsCategoryAndLabelDTO> labelList;
    @ApiModelProperty("规格集合")
    private List<EsGoodsSkuDTO> skuList;
    /**
     * v1.1.0 秒杀商品规格值
     */
    @ApiModelProperty(value = "规格值")
    private String skuValue;
    @ApiModelProperty("属性集合")
    private List<EsGoodsPropertyDTO> propertyList;
    @ApiModelProperty("小程序首页分类对应的商品集合")
    private List<EsIndexCategoryGoodsRelationDTO> indexCategoryGoodsList;
    /**
     * @since V1.2.0
     */
    @ApiModelProperty("1-平台推送2-商家自创")
    private Integer goodsAddType;
    /**
     * @since V1.2.0
     */
    @ApiModelProperty("每单限x份优惠")
    private Integer discountLimit;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "售卖渠道 1.外卖小程序 2:食堂 3.全部")
    private Integer salesChannels;

    @ApiModelProperty(value = "V1.4.0 是否是特价商品 false-否 true-是")
    private Boolean specialState;
    @ApiModelProperty(value = "V1.4.0 特价商品活动ID")
    private String specialId;
    @ApiModelProperty(value = "折扣")
    private BigDecimal specialNumber;

    @ApiModelProperty("门店对应的商品排序")
    private List<GoodsSortDTO> sortDTOS;


    /**
     * 描述: 特价活动起售优先商品起售
     *
     * @date 2020/9/15 14:56
     * @since v1.4.0
     */
    public Integer getLowestBuy(Boolean isCompanyUser) {
        if (isCompanyUser) {
            return lowestBuy;
        }
        return BaseUtil.isNullOrNegativeOne(minQuantity) ? lowestBuy : minQuantity;
    }


    public String getKey() {
        return  goodsId+" "+getShopId();
    }
}
