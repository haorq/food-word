package com.meiyuan.catering.merchant.dto.shop;

import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.validator.CantContainsEmoji;
import com.meiyuan.catering.core.validator.ChineseCharacters;
import com.meiyuan.catering.core.validator.NotSpace;
import com.meiyuan.catering.merchant.dto.merchant.MerchantDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantShopAddDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantEntity;
import com.meiyuan.catering.merchant.entity.CateringShopApplyEntity;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.entity.CateringShopExtEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("小程序商户申请DTO")
public class ShopApplyDTO{


    @ApiModelProperty("店铺名称")
    @NotNull(message = "店铺名称不能为空")
    @NotEmpty(message = "店铺名称不能为空")
    @NotSpace(message = "店铺名称不能包含空格")
    @CantContainsEmoji(message = "店铺名称不能包含表情")
    private String shopName;

    @ApiModelProperty("店铺地址")
    @NotNull(message = "店铺地址不能为空")
    @NotEmpty(message = "店铺地址不能为空")
    private String addressDetail;

    @ApiModelProperty("店铺门牌号(详细地址)")
    @NotNull(message = "详细地址不能为空")
    @NotEmpty(message = "详细地址不能为空")
    @CantContainsEmoji(message = "详细地址不能包含表情")
    private String doorNumber;


    @ApiModelProperty("身份证正面")
    @NotNull(message = "身份证正面不能为空")
    @NotEmpty(message = "身份证正面不能为空")
    private String idCardPositive;

    @ApiModelProperty("身份证反面")
    @NotNull(message = "身份证反面不能为空")
    @NotEmpty(message = "身份证反面不能为空")
    private String idCardBack;

    @ApiModelProperty("手持身份证")
    @NotNull(message = "手持身份证不能为空")
    @NotEmpty(message = "手持身份证不能为空")
    private String idCardWithhand;

    @ApiModelProperty("营业执照")
    @NotNull(message = "营业执照不能为空")
    @NotEmpty(message = "营业执照不能为空")
    private String businessLicense;

    @ApiModelProperty("食品经营许可证")
    @NotNull(message = "食品经营许可证不能为空")
    @NotEmpty(message = "食品经营许可证不能为空")
    private String foodBusinessLicense;

    @ApiModelProperty("开户银行")
    @NotNull(message = "开户银行不能为空")
    @NotEmpty(message = "开户银行不能为空")
//    @ChineseCharacters(message = "开户银行仅支持汉字")
    private String bankName;

    @ApiModelProperty("开户支行")
    @NotNull(message = "开户支行不能为空")
    @NotEmpty(message = "开户支行不能为空")
//    @ChineseCharacters(message = "开户支行仅支持汉字")
    private String bankBranch;

    @ApiModelProperty("开户银行卡号")
    @NotNull(message = "开户银行卡号不能为空")
    @NotEmpty(message = "开户银行卡号不能为空")
//    @Pattern(regexp = "^[0-9]*$",message = "银行卡号请输入数字")
    private String bankCardNumber;

    @ApiModelProperty("申请时的手机号码")
    @NotNull(message = "联系方式不能为空")
    @NotEmpty(message = "联系方式不能为空")
    private String contactPhone;

    @ApiModelProperty("联系人姓名")
    @NotNull(message = "联系人不能为空")
    @NotEmpty(message = "联系人不能为空")
    @NotSpace(message = "联系人姓名不能包含空格")
    private String contactName;


    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("处理状态(1:审核通过,2:审核不通过)")
    private int status;

    @ApiModelProperty("审核不通过原因")
    private String reasonsForFailure;

    private Long applyUserId;
    private Long merchantId;
    private Long shopId;
    private Long updateBy;
    private String mapCoordinate;
    @ApiModelProperty("配送类型： 1-自配送、2-达达配送【1.5.0】")
    private Integer delivery$Type;

    public CateringMerchantEntity toMerchant(CateringShopApplyEntity cateringShopApplyEntity){
        CateringMerchantEntity merchantEntity = new CateringMerchantEntity();

        merchantEntity.setMerchantName(cateringShopApplyEntity.getShopName());
        merchantEntity.setBusinessLicense(cateringShopApplyEntity.getBusinessLicense());
        merchantEntity.setHygieneLicense(cateringShopApplyEntity.getFoodBusinessLicense());
        merchantEntity.setLegalPersonName(cateringShopApplyEntity.getContactName());
        merchantEntity.setIdCardBack(cateringShopApplyEntity.getIdCardBack());
        merchantEntity.setIdCardPositive(cateringShopApplyEntity.getIdCardPositive());
        merchantEntity.setPhone(cateringShopApplyEntity.getContactPhone());
        merchantEntity.setMerchantSource(1);
        merchantEntity.setMerchantAttribute(2);
        merchantEntity.setMerchantService("[1,2]");

        return merchantEntity;
    }
    public MerchantDTO toMerchantDTO(){
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setMerchantName(this.getShopName());
        merchantDTO.setBusinessLicense(this.getBusinessLicense());
        merchantDTO.setHygieneLicense(this.getFoodBusinessLicense());
        merchantDTO.setLegalPersonName(this.getContactName());
        merchantDTO.setIdCardBack(this.getIdCardBack());
        merchantDTO.setIdCardPositive(this.getIdCardPositive());
        merchantDTO.setPhone(this.getContactPhone());
        //1.平台添加,2.商户申请
        merchantDTO.setMerchantSource(2);
        merchantDTO.setMerchantAttribute(2);
        merchantDTO.setMerchantService("[1,2]");
        return merchantDTO;
    }
    public MerchantShopAddDTO toMerchantShopAddDTO(){
        MerchantShopAddDTO merchantShopAddDTO = BaseUtil.objToObj(this,MerchantShopAddDTO.class);

        merchantShopAddDTO.setRegisterPhone(this.getContactPhone());
        merchantShopAddDTO.setPrimaryPersonName(this.getContactName());
        merchantShopAddDTO.setShopPhone(this.getContactPhone());
        merchantShopAddDTO.setOpeningTime("08:00");
        merchantShopAddDTO.setClosingTime("22:59");
        merchantShopAddDTO.setChangeGoodPrice(false);
        //1.平台添加,2.商户申请
        merchantShopAddDTO.setShopSource(2);
        //商户服务:1;外卖小程序,2:食堂美食城（json字符串）
        merchantShopAddDTO.setShopService("[1,2]");
        //门店类型：1-店铺 2-店铺兼自提点 3-自提点
        merchantShopAddDTO.setShopType(1);
        //店铺状态:1：启用，2：禁用
        merchantShopAddDTO.setShopStatus(1);
        //1:自配送，2:达达配送.默认是2达达配送
        merchantShopAddDTO.setDeliveryType(2);
        //地址变了，对应的坐标点也要变
        merchantShopAddDTO.setMapCoordinate(this.getMapCoordinate());
        return merchantShopAddDTO;
    }
    public CateringShopExtEntity toShopExtEntity(){
        CateringShopExtEntity cateringShopExtEntity = new CateringShopExtEntity();
        cateringShopExtEntity.setShopId(this.getShopId());
        cateringShopExtEntity.setBankName(this.getBankName());
        cateringShopExtEntity.setBankBranch(this.getBankBranch());
        cateringShopExtEntity.setBankCardNumber(this.getBankCardNumber());
        cateringShopExtEntity.setAuditStatus(3);
        cateringShopExtEntity.setIdCardBack(this.getIdCardBack());
        cateringShopExtEntity.setIdCardPositive(this.getIdCardPositive());
        cateringShopExtEntity.setIdCardWithhand(this.getIdCardWithhand());
        cateringShopExtEntity.setFoodBusinessLicense(this.getFoodBusinessLicense());
        cateringShopExtEntity.setBusinessLicense(this.getBusinessLicense());
        return cateringShopExtEntity;
    }
    public CateringShopEntity toShop(CateringShopApplyEntity cateringShopApplyEntity){
        CateringShopEntity shopEntity = BaseUtil.objToObj(cateringShopApplyEntity,CateringShopEntity.class);

        shopEntity.setRegisterPhone(cateringShopApplyEntity.getContactPhone());
        shopEntity.setOpeningTime("08:00");
        shopEntity.setClosingTime("22:59");
        shopEntity.setChangeGoodPrice(false);
        shopEntity.setShopSource(2);
        //TODO 何锐 shop表中新增的字段还没有加进去
        return shopEntity;
    }
}
