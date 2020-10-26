package com.meiyuan.catering.merchant.vo.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 查询所有商户VO
 * @Date 2020/3/12 0012 10:32
 */
@Data
@ApiModel("查询所有商户VO")
public class MerchantShopListVo implements Serializable {
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("商家联系人")
    private String primaryPersonName;
    @ApiModelProperty("商家联系人电话")
    private String registerPhone;
    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;
    @ApiModelProperty("完整地址")
    private String addressFull;
    @ApiModelProperty("经纬度")
    private String mapCoordinate;
    @ApiModelProperty("售卖模式 ： 1-菜单售卖模式 2-商品售卖模式")
    private Integer sellType;
    @ApiModelProperty("门店LOGO图片")
    private String doorHeadPicture;
    @ApiModelProperty("门店类型：1-自营 2-入驻 3-自提点")
    private Integer shopType;

    @ApiModelProperty("商家活动赠品库存")
    private Integer giftQuantity;

    @ApiModelProperty("配送价格")
    private BigDecimal deliveryPrice;
    @ApiModelProperty("满单免配送金额")
    private BigDecimal freeDeliveryPrice;
    @ApiModelProperty("订单起送金额")
    private BigDecimal leastDeliveryPrice;
    @ApiModelProperty("配送范围")
    private Integer deliveryRange;
    @ApiModelProperty("商户下已有活动的商品ID列表")
    private List<String> merchantGoodsItem;
}
