package com.meiyuan.catering.es.dto.sku;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.enums.DataType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author wxf
 * @date 2020/3/16 11:33
 * @description 新增/查看商品SKU DTO
 **/
@Data
@ApiModel("新增/查看商品SKU模型")
public class EsGoodsSkuDTO {
    @ApiModelProperty("id 新增不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("商品id 可以不传")
    @ESMapping(datatype = DataType.keyword_type)
    private String goodsId;
    @ApiModelProperty("sku编码 新增不传")
    @ESMapping(datatype = DataType.keyword_type)
    private String skuCode;
    @ApiModelProperty("规格值")
    @ESMapping(datatype = DataType.keyword_type)
    private String propertyValue;
    @ApiModelProperty("原价")
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private BigDecimal marketPrice;
    @ApiModelProperty("特价活动价 （V1.4.0版本为特价商品活动折扣计算后的价格 ）")
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private BigDecimal enterprisePrice;
    @ApiModelProperty("最高购买限制 1-无限制 2-自定义")
    @ESMapping(datatype = DataType.integer_type)
    private Integer buyLimitType;
    @ApiModelProperty("最低购买")
    @ESMapping(datatype = DataType.integer_type)
    private Integer lowestBuy;
    @ApiModelProperty("最多购买,-1表示无限制")
    @ESMapping(datatype = DataType.integer_type)
    private Integer highestBuy;
    @ApiModelProperty(hidden = true, value = "false-没有删除 true-删除")
    private Boolean del;
    @ApiModelProperty("1.外卖小程序 2:食堂 3.全部")
    @ESMapping(datatype = DataType.integer_type)
    private Integer salesChannels;
    @ApiModelProperty("商品单位，例如件、盒( 天，月，年)")
    @ESMapping(datatype = DataType.keyword_type)
    private String unit;
    @ApiModelProperty("每日库存")
    @ESMapping(datatype = DataType.integer_type)
    private Integer stock;
    @ApiModelProperty("是否次日自动自满库存 0-false 1-true")
    @ESMapping(datatype = DataType.boolean_type)
    private Boolean isFullStock;
    /**
     * 每单限制优惠 不传默认每单限制0
     */
    @ApiModelProperty("每单限制优惠 （V1.4.0版本为特价商品活动中的特价商品限优惠数量）")
    @ESMapping(datatype = DataType.integer_type)
    private Integer discountLimit;
    @ApiModelProperty("购物车选择数量")
    private Integer selectedNum;
    @ApiModelProperty(value = "1-统一规格 2-多规格")
    @ESMapping(datatype = DataType.integer_type)
    private Integer goodsSpecType;
    @ApiModelProperty(value = "V1.4.0 特价商品活动中的特价商品起售数量")
    @ESMapping(datatype = DataType.integer_type)
    private Integer minQuantity;
    @ApiModelProperty(value = "V1.4.0 特价商品活动中的特价商品个人折扣值")
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private BigDecimal specialNumber;
    @ApiModelProperty(value = "V1.4.0 特价商品活动定价方式 1-统一折扣 2-折扣 3-固定价")
    @ESMapping(datatype = DataType.integer_type)
    private Integer specialFixType;
    @ApiModelProperty(value = "V1.4.0 特价商品活动ID")
    @ESMapping(datatype = DataType.keyword_type)
    private String specialId;
    @ApiModelProperty(value = "V1.4.0 是否是特价商品 false-否 true-是")
    private Boolean specialState;
    @ApiModelProperty(value = "V1.5.0 打包费 ")
    @ESMapping(datatype = DataType.double_type)
    private BigDecimal packPrice;

    /**
     * 描述:todo sku的特价标识通过判断是否存在特价活动id判断
     *
     * @date 2020/9/16 16:17
     * @since v1.4.0
     */
    public Boolean getSpecialState() {
        return specialId != null && !Objects.equals(specialId, BaseUtil.CHANGE_FLAG);
    }

    public EsGoodsSkuDTO() {
        this.buyLimitType = 1;
        this.highestBuy = -1;
        this.discountLimit = -1;
        this.minQuantity = -1;
        this.specialFixType = -1;
        this.specialId = "-1";
    }
}
