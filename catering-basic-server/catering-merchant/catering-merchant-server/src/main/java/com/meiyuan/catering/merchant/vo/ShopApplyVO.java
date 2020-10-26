package com.meiyuan.catering.merchant.vo;

import com.meiyuan.catering.merchant.entity.CateringShopExtEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("店铺入驻列表")
public class ShopApplyVO {

    @ApiModelProperty("数据表Id")
    private String id;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("申请时的手机号码")
    private String contactPhone;

    @ApiModelProperty("联系人姓名")
    private String contactName;

    @ApiModelProperty("身份证正面")
    private String idCardPositive;

    @ApiModelProperty("身份证反面")
    private String idCardBack;

    @ApiModelProperty("手持身份证")
    private String idCardWithhand;

    @ApiModelProperty("营业执照")
    private String businessLicense;

    @ApiModelProperty("食品经营许可证")
    private String foodBusinessLicense;

    @ApiModelProperty("详细地址")
    private String addressDetail;

    @ApiModelProperty("完整地址")
    private String addressFull;

    @ApiModelProperty("门牌号")
    private String doorNumber;

    @ApiModelProperty("经纬度")
    private String mapCoordinate;

    @ApiModelProperty("开户银行")
    private String bankName;

    @ApiModelProperty("开户支行")
    private String bankBranch;

    @ApiModelProperty("开户银行卡号")
    private String bankCardNumber;

    @ApiModelProperty("是否处理(1:待处理,2:已处理)")
    private int handled;


    @ApiModelProperty("处理状态(1:审核通过,2:审核不通过)")
    private int status;

    @ApiModelProperty("审核不通过原因")
    private String reasonsForFailure;

    @ApiModelProperty("审核人ID")
    private Long updateBy;

    @ApiModelProperty("创建时间(申请时间)")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间(处理时间)")
    private LocalDateTime updateTime;


    @ApiModelProperty("详情-处理结果-门店编码")
    private String shopCode;
    @ApiModelProperty("详情-处理结果-门店名称")
    private String shopNameAfterAudit;
    @ApiModelProperty("详情-处理结果-品牌名称")
    private String merchantNameAfterAudit;
    @ApiModelProperty("详情-处理结果-品牌编码")
    private String merchantCode;

    public ShopApplyVO fromShopExt(CateringShopExtEntity extEntity){
        if(extEntity != null){
            this.setBankName(this.getBankName());
            this.setBankBranch(this.getBankBranch());
            this.setBankCardNumber(this.getBankCardNumber());
//        this.setAuditStatus(3);
            this.setIdCardBack(this.getIdCardBack());
            this.setIdCardPositive(this.getIdCardPositive());
            this.setIdCardWithhand(this.getIdCardWithhand());
            this.setFoodBusinessLicense(this.getFoodBusinessLicense());
            this.setBusinessLicense(this.getBusinessLicense());
        }
        return this;
    }

}
