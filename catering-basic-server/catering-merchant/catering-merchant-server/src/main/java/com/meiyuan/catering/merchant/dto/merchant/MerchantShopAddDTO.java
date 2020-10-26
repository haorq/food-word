package com.meiyuan.catering.merchant.dto.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopTagsVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/7/3 0003 10:54
 * @Description 简单描述 :  商户-店铺添加DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("商户-店铺添加DTO")
public class MerchantShopAddDTO{
    @ApiModelProperty("商户id【1.2.0】")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty("店铺类型：1-店铺 2-店铺兼自提点 3-自提点")
    private Integer shopType;
    @ApiModelProperty("是否是自提点：0- 不是 1-是【1.1.0】")
    private Boolean isPickupPoint;

    @ApiModelProperty("门店服务【1.2.0】")
    private String shopService;

    @ApiModelProperty("店铺状态:1:启用；2:禁用；【1.2.0】")
    private Integer shopStatus;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺logo")
    private String doorHeadPicture;

    @ApiModelProperty("联系人姓名")
    private String primaryPersonName;
    @ApiModelProperty("注册电话/店铺联系人电话")
    private String registerPhone;
    @ApiModelProperty("门店电话")
    private String shopPhone;

    @ApiModelProperty("营业开始时间")
    private String openingTime;
    @ApiModelProperty("营业结束时间")
    private String closingTime;

    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("店铺门牌号")
    private String doorNumber;
    @ApiModelProperty("经纬度")
    private String mapCoordinate;

    @ApiModelProperty("门店公告【1.2.0】")
    private String shopNotice;

    @ApiModelProperty("是否可修改商品价格：0：否，1：是【1.2.0】")
    private Boolean changeGoodPrice;

    @ApiModelProperty("营业执照")
    private String businessLicense;
    @ApiModelProperty("食品经营许可证")
    private String foodBusinessLicense;



    @ApiModelProperty("店铺标签")
    private List<MerchantShopTagsVo> merchantShopTags;
    @ApiModelProperty("身份证正面")
    private String idCardPositive;
    @ApiModelProperty("身份证反面")
    private String idCardBack;
    @ApiModelProperty("店铺保证金")
    private BigDecimal earnestMoney;
    @ApiModelProperty("店铺编码")
    private String shopCode;
    @ApiModelProperty("门店来源：1-pc端平台添加 2- 商户申请【1.5.0】")
    private Integer shopSource;


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


}
