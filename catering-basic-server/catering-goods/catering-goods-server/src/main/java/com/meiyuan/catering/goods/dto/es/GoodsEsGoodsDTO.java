package com.meiyuan.catering.goods.dto.es;

import com.meiyuan.catering.goods.dto.category.UpdateGoodsSortDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsSortDTO;
import com.meiyuan.catering.goods.dto.property.GoodsPropertyDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class GoodsEsGoodsDTO {
    /**
     * 这个字段 团购/秒杀/用户购物车 判断标识
     */
    @ApiModelProperty(hidden = true, value = "新增 true 反则 修改")
    private Boolean flag;
    @ApiModelProperty(hidden = true, value = "是否为商品排序")
    private Boolean isGoodsSort;
    @ApiModelProperty("id")
    private Long id;
    private Long goodsId;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("门店名称")
    private String shopName;
    @ApiModelProperty("名称")
    private String goodsName;
    @ApiModelProperty("菜品编号")
    private String spuCode;
    @ApiModelProperty("菜品分类id")
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("分类商品排序号")
    private Integer categoryGoodsSort;
    @ApiModelProperty("列表图片")
    private String listPicture;
    @ApiModelProperty("详情图片")
    private String infoPicture;
    @ApiModelProperty("门店上下架 1-下架,2-上架")
    private Integer goodsStatus;
    @ApiModelProperty("商户上下架 1-下架,2-上架")
    private Integer merchantGoodsStatus;
    @ApiModelProperty("原价(市场价)")
    private Double marketPrice;
    @ApiModelProperty("销售价")
    private Double salesPrice;
    @ApiModelProperty("企业价")
    private Double enterprisePrice;
    @ApiModelProperty("商品详细介绍")
    private String goodsDescribeText;

    @ApiModelProperty("商品简介")
    private String goodsSynopsis;

    @ApiModelProperty("商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("销量")
    private Long salesCount;
    @ApiModelProperty("月销量")
    private Long monthSalesCount;
    @ApiModelProperty(value = "是否预售0-否 1-是")
    private Boolean presellFlag;
    @ApiModelProperty("预售时间  日期是否勾选")
    private Boolean isCheck;
    @ApiModelProperty("开始售卖时间（yyyy-mm-dd）")
    private LocalDate startSellTime;
    @ApiModelProperty("结束售卖时间（yyyy-mm-dd）")
    private LocalDate endSellTime;
    @ApiModelProperty("星期一到天（1-7）")
    private String sellWeekTime;
    @ApiModelProperty("当天截止售卖时间（hh:ss）")
    private String closeSellTime;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("商户商品标识 true-是推送给商家的商品 false-单纯的商品信息")
    private Boolean merchantGoodsFlag;
    @ApiModelProperty(hidden = true, value = "false-没有删除 true-删除")
    private Boolean del;
    @ApiModelProperty("标签集合")
    private List<GoodsCategoryAndLabelDTO> labelList;
    @ApiModelProperty("规格集合")
    private List<GoodsSkuDTO> skuList;
    @ApiModelProperty("属性集合")
    private List<GoodsPropertyDTO> propertyList;
    @ApiModelProperty("1-平台推送2-商家自创")
    private Integer goodsAddType;
    @ApiModelProperty("每单限制多少优惠")
    private Integer discountLimit;


    private String location;

    @ApiModelProperty("省")
    private String provinceCode;

    @ApiModelProperty("市")
    private String esCityCode;

    @ApiModelProperty("区")
    private String areaCode;

    @ApiModelProperty("门店对应的商品排序")
    private List<GoodsSortDTO> sortDTOS;


    public GoodsEsGoodsDTO() {
        this.isCheck = false;
    }
}
