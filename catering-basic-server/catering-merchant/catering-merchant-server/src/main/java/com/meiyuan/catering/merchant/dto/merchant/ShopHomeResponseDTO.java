package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 店铺首页信息
 * @Date  2020/3/24 0024 12:04
 */
@Data
public class ShopHomeResponseDTO implements Serializable {
    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "店铺名称/自提点名称")
    private String shopName;

    @ApiModelProperty(value = "门店头像/门店LOGO")
    private String doorHeadPicture;

    @ApiModelProperty(value = "门头图片(1.0.1)")
    private String shopPicture;

    @ApiModelProperty(value = "营业状态：1： 营业中、2：已打烊、3：停业中【1.5.0】")
    private Integer businessStatus;

    @ApiModelProperty(value = "售卖模式：1-商品售卖模式 2-菜单售卖模式")
    private Integer sellType;

    @ApiModelProperty(value = "业务支持：1：仅配送，2：仅自提，3：全部")
    private Integer businessSupport;

    @ApiModelProperty(value = "自提点/门店联系人")
    private String primaryPersonName;

    @ApiModelProperty(value = "自提点/门店注册电话、负责人电话")
    private String registerPhone;

    @ApiModelProperty(value = "完整地址")
    private String addressFull;

    @ApiModelProperty(value = "门店公告")
    private String shopNotice;

    @ApiModelProperty(value = "门店电话")
    private String shopPhone;

    @ApiModelProperty(value = "门店详细地址")
    private String addressDetail;

    @ApiModelProperty(value = "店铺门牌号")
    private String doorNumber;

    @ApiModelProperty(value = "经纬度")
    private String mapCoordinate;

    @ApiModelProperty(value = "营业开始时间【1.2.0】")
    private String openingTime;

    @ApiModelProperty(value = "营业结束时间【1.2.0】")
    private String closingTime;

    @ApiModelProperty(value = "店铺状态:1：启用，2：禁用【1.2.0】")
    private Integer shopStatus;

    @ApiModelProperty(value = "品牌属性:1:自营，2：非自营【1.2.0】")
    private Integer merchantAttribute;

    @ApiModelProperty(value = "门店类型:1:店铺，2：店铺兼自提点，3，自提点【1.2.0】")
    private Integer shopType;

    @ApiModelProperty(value = "店铺,自提点地址图片(1.1.0)")
    private List<String> addressPictureList;

    @ApiModelProperty(value = "店铺,自提点地址图片(1.1.0)")
    private List<ShopAddressPictureDTO> addressPictureDTOS;
}
