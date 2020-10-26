package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("catering_shop_apply")
public class CateringShopApplyEntity extends IdEntity
        implements Serializable {


    /**
     * 申请人用户ID
     */
    @TableField(value = "apply_user_id")
    private Long applyUserId;
    /**
     * 店铺名称
     */
    @TableField(value = "shop_name")
    private String shopName;

    /**
     * 申请时的手机号码,默认联系方式不可修改
     */
    @TableField(value = "contact_phone")
    private String contactPhone;
    /**
     * 联系人姓名
     */
    @TableField(value = "contact_name")
    private String contactName;

    /**
     * 身份证正面
     */
    @TableField(value = "id_card_positive")
    private String idCardPositive;
    /**
     * 身份证反面
     */
    @TableField(value = "id_card_back")
    private String idCardBack;

    /**
     * 手持身份证
     */
    @TableField(value = "id_card_withhand")
    private String idCardWithhand;

    /**
     * 营业执照
     */
    @TableField(value = "business_license")
    private String businessLicense;
    /**
     * 食品经营许可证
     */
    @TableField(value = "food_business_license")
    private String foodBusinessLicense;


    /**
     * 省
     */
    @TableField(value = "address_province_code")
    private String addressProvinceCode;
    /**
     * 市
     */
    @TableField(value = "address_city_code")
    private String addressCityCode;
    /**
     * 区
     */
    @TableField(value = "address_area_code")
    private String addressAreaCode;
    /**
     * 地址省
     */
    @TableField(value = "address_province")
    private String addressProvince;
    /**
     * 地址市
     */
    @TableField(value = "address_city")
    private String addressCity;
    /**
     * 地址区
     */
    @TableField(value = "address_area")
    private String addressArea;
    /**
     * 详细地址
     */
    @TableField(value = "address_detail")
    private String addressDetail;
    /**
     * 完整地址
     */
    @TableField(value = "address_full")
    private String addressFull;
    /**
     * 门牌号
     */
    @TableField(value = "door_number")
    private String doorNumber;
    /**
     * 经纬度
     */
    @TableField(value = "map_coordinate")
    private String mapCoordinate;


    /**
     * 开户银行
     */
    @TableField(value = "bank_name")
    private String bankName;

    /**
     * 开户支行
     */
    @TableField(value = "bank_branch")
    private String bankBranch;

    /**
     * 开户银行卡号
     */
    @TableField(value = "bank_card_number")
    private String bankCardNumber;

    /**
     * 是否处理: 0-否 1-是
     */
    @TableField(value = "handled")
    private int handled;

    /**
     * 处理状态(1:审核通过,2:审核不通过)
     */
    @TableField(value = "status")
    private int status;

    /**
     * 审核不通过原因
     */
    @TableField(value = "reasons_for_failure")
    private String reasonsForFailure;

    /**
     * 审核人ID
     */
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(value = "shop_id")
    private Long shopId;

    @TableField(value = "merchant_id")
    private Long merchantId;

}
