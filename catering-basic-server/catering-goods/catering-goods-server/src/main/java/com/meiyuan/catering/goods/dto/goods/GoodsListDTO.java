package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BigDecimalSort;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author wxf
 * @date 2020/3/16 18:25
 * @description 商品列表DTO
 **/
@Data
@ApiModel("商品列表分页返回数据模型")
public class GoodsListDTO {
    @ApiModelProperty("商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("菜品编号")
    private String spuCode;
    @ApiModelProperty(value = "商品列表图")
    private String listPicture;
    @ApiModelProperty(value = "是否允许改价 0：否，1：是")
    private Boolean changeGoodPrice;
    @ApiModelProperty("菜品名称")
    private String goodsName;
    @ApiModelProperty("分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("分类名称")
    private String categoryName;
    @ApiModelProperty("原价(市场价)")
    private BigDecimal marketPrice;
    @ApiModelProperty("最大原价是否null")
    private Boolean nullMaxMarketPrice;
    @ApiModelProperty("最小原价是否null")
    private Boolean nullMinMarketPrice;
    @ApiModelProperty("销售价 === -1表示不存在现价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价 === -1表示不存在企业价")
    private BigDecimal enterprisePrice;
    @ApiModelProperty("销量")
    private Long salesCount;
    @ApiModelProperty("商户 1-下架,2-上架")
    private Integer platformGoodsStatus;
    @ApiModelProperty("门店上下架 1-下架,2-上架")
    private Integer goodsStatus;
    @ApiModelProperty("固定商家还是全部商家 -1-没有推送 1-所有商家 2-指定商家")
    private Integer fixedOrAll;
    @ApiModelProperty("1-平台推送2-商家自创3-门店自创")
    private Integer goodsAndType;
    @ApiModelProperty("最大-原价(市场价)")
    private BigDecimal maxMarketPrice;
    @ApiModelProperty("最大-销售价")
    private BigDecimal maxSalesPrice;
    @ApiModelProperty("最大-企业价")
    private BigDecimal maxEnterprisePrice;
    @ApiModelProperty("商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("剩余库存：为0表示已售罄；-1是不限")
    private Integer remainStock;

    @ApiModelProperty("商品排序号")
    private Integer sort;


    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    public String getListPicture(){
        if(StringUtils.isNotBlank(this.listPicture)){
            return StringUtils.substringBefore(this.listPicture,",");
        }
        return StringUtils.EMPTY;
    }
}
