package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @author yaoozu
 * @description 店铺信息
 * @date 2020/3/2011:39
 * @since v1.0.0
 */
@Data
@ApiModel("店铺信息")
public class ShopResponseDTO {
    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty(value = "营业状态：1-营业 2-打样")
    private Integer businessStatus;

    @ApiModelProperty(value = "门店LOGO")
    private String  shopLogo;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "门店电话")
    private String shopPhone;

    @ApiModelProperty(value = "类型：1--店铺，2--自提点，3--既是店铺也是自提点,4-员工")
    private Integer type;

    @ApiModelProperty(value = "店铺状态:1： 营业中、2：已打烊、3：停业中")
    private Integer shopStatus;

    @ApiModelProperty(value = "地址省")
    private String addressProvince;
    @ApiModelProperty(value = "地址市")
    private String addressCity;
    @ApiModelProperty(value = "地址区")
    private String addressArea;
    @ApiModelProperty(value = "详细地址")
    private String addressDetail;
    @ApiModelProperty(value = "店铺门牌号")
    private String doorNumber;
    @ApiModelProperty(value = "经纬度：经度,纬度")
    private String mapCoordinate;

    @ApiModelProperty(value = "门店公告")
    private String shopNotice;

    @ApiModelProperty(value = "门店注册电话（门店登录账号）")
    private String registerPhone;
    @ApiModelProperty(value = "登录手机号")
    private String loginPhone;
    @ApiModelProperty(value = "门店联系人姓名")
    private String primaryPersonName;

    @ApiModelProperty(value = "是否能够切换登录模式:true:是，false：否")
    private Boolean changeRole;
    @ApiModelProperty(value = "店铺,自提点地址图片(1.1.0)")
    private List<String> addressPictureList;
    @ApiModelProperty(value = "是否是首次登录:true:是，false：否(1.1.0)")
    private Boolean firstLogin;
}
