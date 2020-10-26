package com.meiyuan.catering.wx.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.dto.goods.EsGoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.enums.GoodsSpecTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/23 11:49
 * @description 简单描述
 **/
@Data
@ApiModel("商品模型")
public class WxMerchantGoodsDTO {
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("esGoodsId")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long esGoodsId;
    @ApiModelProperty("goodsId")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("名称")
    private String goodsName;
    @ApiModelProperty("菜品编号")
    private String spuCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("菜品分类id")
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("菜单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
    @ApiModelProperty("列表图片")
    private String listPicture;
    @ApiModelProperty("详情图片")
    private String infoPicture;

    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("现价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;

    @ApiModelProperty("商品详细介绍")
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
    @ApiModelProperty("月销量")
    private Long monthSalesCount;

    @ApiModelProperty(hidden = true, value = "根据sku分组使用的字段")
    private String skuCode;
    @ApiModelProperty("标签集合")
    private List<EsGoodsCategoryAndLabelDTO> labelList;
    @ApiModelProperty("规格集合")
    private List<WxMerchantGoodsSkuDTO> skuList;

    @ApiModelProperty("加入购物车数量")
    private Integer selectedNum;

    @ApiModelProperty("折扣")
    private String discountLabel;

    @ApiModelProperty("是否为预售")
    private Boolean presellFlag;

    @ApiModelProperty("开始售卖时间(yyyy-mm-dd)")
    private Date startSellTime;

    @ApiModelProperty("结束售卖时间(yyyy-mm-dd)")
    private Date endSellTime;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("星期（1-7）")
    private String sellWeekTime;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("当天截止售卖时间(hh:ss)")
    private String closeSellTime;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "剩余库存")
    private Integer residualInventory;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "每单限x份优惠")
    private Integer discountLimit;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "售卖渠道 1.外卖小程序 2:食堂 3.全部")
    private Integer salesChannels;

    @ApiModelProperty(value = "V1.4.0 是否是特价商品 false-否 true-是")
    private Boolean specialState;

    /**
     * 描述: 多规格 库存累加
     *
     * @since v1.2.0
     */
    public Integer getResidualInventory() {
        if (Objects.equals(goodsSpecType, GoodsSpecTypeEnum.MANY_SPEC.getStatus())) {
            List<Integer> collect = skuList.stream().map(WxMerchantGoodsSkuDTO::getResidualInventory).collect(Collectors.toList());
            if (collect.contains(BaseUtil.DEFAULT_INVENTORY)) {
                return BaseUtil.DEFAULT_INVENTORY;
            }
            residualInventory = skuList.stream().filter(sku -> sku.getResidualInventory() != null).mapToInt(WxMerchantGoodsSkuDTO::getResidualInventory).sum();
        } else {
            residualInventory = skuList.get(0).getResidualInventory();
        }
        return residualInventory;
    }

    public String getListPicture() {
        if (infoPicture.contains(BaseUtil.COMMA)) {
            return infoPicture.split(BaseUtil.COMMA)[0];
        }
        return infoPicture;
    }
}
