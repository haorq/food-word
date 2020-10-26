package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Description 店铺自提点关联关系表
 * @Date  2020/3/11 0011 10:56
 */
@Data
@TableName("catering_merchant_pickup_adress")
public class CateringMerchantPickupAdressEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = 116397391775196435L;
     /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 自提点id
     */
    @TableField(value = "pickup_id")
    private Long pickupId;

    /**
     * 负责人姓名
     */
    @TableField(value = "charger_name")
    private String chargerName;

    /**
     * 负责人电话
     */
    @TableField(value = "charger_phone")
    private String chargerPhone;


    /**
     * 自提点名
     */
    @TableField(value = "name")
    private String name;
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
     * 经纬度
     */
    @TableField(value = "map_coordinate")
    private String mapCoordinate;

    /**
     * 店铺状态:1：启用，2：禁用
     */
    @TableField(value = "shop_status")
    private Integer shopStatus;

    /**
     * 是否删除
     */
    @TableField(value = "is_del")
    private Boolean isDel;
}