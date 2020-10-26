package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.goods.dto.merchant.MerchantDTO;
import com.meiyuan.catering.goods.dto.property.GoodsPropertyDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/16 11:16
 * @description 新增/查看商品DTO
 **/
@Data
@ApiModel("新增/查看商品模型")
public class GoodsDTO {
    @ApiModelProperty("id 新增不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("自动编号 true-自动 false-手动")
    private Boolean autoCode;
    @ApiModelProperty("菜品编号")
    private String spuCode;
    @ApiModelProperty("菜品名称")
    private String goodsName;
    @ApiModelProperty("菜品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("标签id集合")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> labelIdList;
    @ApiModelProperty("标签名集合")
    private List<String> labelNameList;
    @ApiModelProperty("列表图片")
    private String listPicture;
    @ApiModelProperty("详情图片")
    private String infoPicture;
    @ApiModelProperty("详情图片集合")
    private String[] infoPictureList;
    @ApiModelProperty("平台上下架 1-下架,2-上架")
    private Integer goodsStatus;
    @ApiModelProperty("原价(市场价)")
    private BigDecimal marketPrice;
    @ApiModelProperty("销售价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;
    @ApiModelProperty("商品详细介绍，是富文本格式")
    private String goodsDescribeText;
    @ApiModelProperty("商品简介")
    private String goodsSynopsis;
    @ApiModelProperty("最高购买限制 1-无限制 2-自定义")
    private Integer buyLimitType;
    @ApiModelProperty("最低购买")
    private Integer lowestBuy;
    @ApiModelProperty("最多购买 -1无限制")
    private Integer highestBuy;
    @ApiModelProperty("商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("1-平台 2-店铺")
    private Integer goodsAddType;
    @ApiModelProperty("开始售卖时间")
    private LocalDateTime startSellTime;
    @ApiModelProperty("结束售卖时间")
    private LocalDateTime endSellTime;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("规格集合")
    private List<GoodsSkuDTO> skuList;
    @ApiModelProperty("属性集合")
    private List<GoodsPropertyDTO> propertyList;
    @ApiModelProperty("推送的商家集合")
    private List<MerchantDTO> merchantList;
}
