package com.meiyuan.catering.merchant.dto.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author yaoozu
 * @description 商户基本信息
 * @date 2020/3/2019:24
 * @since v1.0.0
 */
@Data
@ApiModel("商户基本信息")
public class MerchantModifyInfoRequestDTO {
    @ApiModelProperty(value = "门店公告")
    private String shopNotice;

    @ApiModelProperty(value = "门店电话")
    private String shopPhone;

    @ApiModelProperty(value = "门店LOGO")
    private String  shopLogo;

    @ApiModelProperty(value = "门头图片")
    private String shopPicture;

    @ApiModelProperty(value = "百度地图获取的完整地址")
    private String addressDetail;
    @ApiModelProperty(value = "门牌号")
    private String doorNumber;
    @ApiModelProperty(value = "百度地图获取经纬度：经度,纬度")
    private String mapCoordinate;

    @ApiModelProperty(value = "店铺,自提点地址图片(1.1.0)")
    @Size(max = 5, message = "门店照片最多上传5张")
    private List<String> addressPictureList;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;

    @ApiModelProperty("店铺联系人")
    private String primaryPersonName;

    @ApiModelProperty("联系人电话")
    private String registerPhone;

    @ApiModelProperty("营业开始时间")
    private String openingTime;

    @ApiModelProperty("营业结束时间")
    private String closingTime;

    @ApiModelProperty("门店类型：1-店铺 2-店铺兼自提点 3-自提点")
    private Integer shopType;

    @ApiModelProperty("是否可修改商品价格：0：否，1：是 [1.2.0]")
    private Boolean changeGoodPrice;

    @ApiModelProperty("营业执照")
    private String businessLicense;

    @ApiModelProperty("卫生许可证")
    private String foodBusinessLicense;

    @ApiModelProperty("配送类型： 1-自配送、2-达达配送【1.5.0】")
    private Integer deliveryType;
    @ApiModelProperty("手持身份证【1.5.0】")
    private String idCardWithhand;
    @ApiModelProperty("开户银行【1.5.0】")
    private String bankName;
    @ApiModelProperty("开户支行【1.5.0】")
    private String bankBranch;
    @ApiModelProperty("开户银行卡号【1.5.0】")
    private String bankCardNumber;
    @ApiModelProperty("门店来源：1-pc端平台添加 2- 商户申请【1.5.0】")
    private Integer shopSource;
    @ApiModelProperty(value = "身份证正面【1.5.0】")
    private String idCardPositive;

    @ApiModelProperty(value = "身份证反面【1.5.0】")
    private String idCardBack;
}
