package com.meiyuan.catering.wx.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 微信商户基本信息
 * @date 2020/3/2814:41
 * @since v1.0.0
 */
@Data
@ApiModel("微信商户基本信息dto")
public class WxMerchantBaseInfoDTO {

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "完整地址")
    private String addressFull;

    @ApiModelProperty(value = "门店联系电话")
    private String shopPhone;

    @ApiModelProperty(value = "门店LOGO图片")
    private String doorHeadPicture;

    @ApiModelProperty(value = "门店头图")
    private String shopPicture;

    @ApiModelProperty(value = "距离 文字")
    private String distanceStr;

    @ApiModelProperty(value = "公告")
    private String shopNotice;

    @ApiModelProperty(value = "食品经营许可证")
    private String foodBusinessLicense;

    @ApiModelProperty(value = "营业执照")
    private String businessLicense;

    @ApiModelProperty("售卖模式： 1-菜单售卖模式 2-商品售卖模式")
    private Integer sellType;

    @ApiModelProperty("门店类型：1-自营 2-入驻 3-自提点")
    private Integer shopType;

    @ApiModelProperty("营业状态：1-营业 2-打样")
    private Integer businessStatus;

    /**
     * @since v1.2.0
     */
    @ApiModelProperty("品牌属性:1:自营，2:非自营")
    private Integer merchantAttribute;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("营业开始时间")
    private String openingTime;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("营业结束时间")
    private String closingTime;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("店铺状态:1：启用，2：禁用,3：删除")
    private Integer shopStatus;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("商户状态：1- 禁用 2-启用")
    private Integer merchantStatus;


    public WxMerchantBaseInfoDTO() {
        this.shopNotice = "";
        this.shopPhone = "";
    }
}
