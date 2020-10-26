package com.meiyuan.catering.merchant.vo.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author MeiTao
 * @Description 商户信息VO
 * @Date 2020/3/12 0012 10:32
 */
@Data
@ApiModel("商户信息VO")
public class MerchantInfoVo implements Serializable {
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
    @ApiModelProperty("省编码")
    private String provinceCode;
    @ApiModelProperty("市编码")
    private String cityCode;
    @ApiModelProperty("区编码")
    private String areaCode;
    @ApiModelProperty("售卖模式 ： 1-菜单售卖模式 2-商品售卖模式")
    private Integer sellType;
    @ApiModelProperty("门店LOGO图片")
    private String doorHeadPicture;
    @ApiModelProperty("门店类型：1-自营 2-入驻 3-自提点")
    private Integer shopType;


    @ApiModelProperty("1-自动 2-不自动")
    private Integer autoReceipt;
    @ApiModelProperty("配送对象：1-企业 2-个人 3-全部")
    private Integer deliveryObject;
    @ApiModelProperty("配送价格")
    private BigDecimal deliveryPrice;
    @ApiModelProperty("满单免配送金额")
    private BigDecimal freeDeliveryPrice;
    @ApiModelProperty("订单起送金额")
    private BigDecimal leastDeliveryPrice;
    @ApiModelProperty("配送范围")
    private Integer deliveryRange;
    @ApiModelProperty("配送范围规则:1-距离 2-直线")
    private Integer deliveryRule;

    @ApiModelProperty("业务支持：1：仅配送，2：仅自提，3：全部")
    private Integer businessSupport;
}
