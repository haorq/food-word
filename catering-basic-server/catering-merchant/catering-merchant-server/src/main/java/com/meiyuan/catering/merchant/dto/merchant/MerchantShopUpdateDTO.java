package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopTagsVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 商户店铺修改DTO
 * @Date  2020/3/16 0016 14:53
 */
@Data
@ApiModel("商户店铺修改DTO")
public class MerchantShopUpdateDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;

    @ApiModelProperty("门店LOGO图片")
    private String doorHeadPicture;

    @ApiModelProperty("店铺联系人")
    private String primaryPersonName;

    @ApiModelProperty("联系人电话")
    private String registerPhone;

    @ApiModelProperty("门店联系电话")
    private String shopPhone;

    @ApiModelProperty("营业开始时间")
    private String openingTime;

    @ApiModelProperty("营业结束时间")
    private String closingTime;

    @ApiModelProperty("店铺门牌号")
    private String doorNumber;

    @ApiModelProperty("详细地址")
    private String addressDetail;

    @ApiModelProperty("经纬度")
    private String mapCoordinate;

    @ApiModelProperty("门店公告")
    private String shopNotice;

    @ApiModelProperty("门店类型：1-店铺 2-店铺兼自提点 3-自提点")
    private Integer shopType;

    @ApiModelProperty("是否可修改商品价格：false：否，true：是 [1.2.0]")
    private Boolean changeGoodPrice;

    @ApiModelProperty("营业执照")
    private String businessLicense;

    @ApiModelProperty("卫生许可证")
    private String foodBusinessLicense;

    @ApiModelProperty("身份证正面【1.1.0】")
    private String idCardPositive;
    @ApiModelProperty("身份证反面【1.1.0】")
    private String idCardBack;
    @ApiModelProperty("门店保证金")
    private BigDecimal earnestMoney;
    @ApiModelProperty("店铺标签")
    private List<MerchantShopTagsVo> merchantShopTags;

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

}
