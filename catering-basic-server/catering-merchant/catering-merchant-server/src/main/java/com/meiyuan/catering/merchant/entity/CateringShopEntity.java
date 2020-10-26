package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 店铺表(CateringShop)实体类
 *
 * @author wxf
 * @since 2020-03-10 10:13:13
 */
@Data
@TableName("catering_shop")
public class CateringShopEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = -85587825409728230L;
     /**
     * 商户id
     */
     @TableField(value = "merchant_id")
    private Long merchantId;
    /**
     * 店铺编码
     */
    @TableField(value = "shop_code")
    private String shopCode;
     /**
     * 店铺名称
     */
     @TableField(value = "shop_name")
    private String shopName;
    /**
     * 门店联系电话
     */
    @TableField(value = "shop_phone")
    private String shopPhone;
     /**
     * 门店LOGO图片
     */
     @TableField(value = "door_head_picture")
    private String doorHeadPicture;
     /**
     * 店面图片
     */
     @TableField(value = "shop_picture")
    private String shopPicture;

    /**
     * 门店登陆密码
     */
    @TableField(value = "password")
    private String password;
    /**
     * 门店联系人
     */
    @TableField(value = "primary_person_name")
    private String primaryPersonName;

    /**
     * 门店注册电话（门店登录账号）
     */
    @TableField(value = "register_phone")
    private String registerPhone;
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
     * 门店来源：1- 商户申请 2-平台添加
     */
    @TableField(value = "shop_source")
    private Integer shopSource;
    /**
     * 是否是自提点：0- 不是 1-是
     */
    @TableField(value = "is_pickup_point")
    private Boolean isPickupPoint;
    /**
     * 门店保证金
     */
    @TableField(value = "earnest_money")
    private BigDecimal earnestMoney;

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
     * 门店类型：1-店铺 2-店铺兼自提点 3-自提点
     */
     @TableField(value = "shop_type")
    private Integer shopType;
     /**
     * 营业开始时间
     */
     @TableField(value = "opening_time")
    private String openingTime;
     /**
     * 营业结束时间
     */
     @TableField(value = "closing_time")
    private String closingTime;
     /**
     * 营业状态：1-营业 2-打样
     */
     @TableField(value = "business_status")
    private Integer businessStatus;

    /**
     * 店铺状态:1：启用，2：禁用
     */
    @TableField(value = "shop_status")
    private Integer shopStatus;
    /**
     * 0-否 1-是
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    private Boolean del;
    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 最近一次登录IP地址
     */
    @TableField(value = "last_login_ip")
    private String lastLoginIp;
    /**
     * 最近一次登录时间
     */
    @TableField(value = "last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 门店公告
     */
    @TableField(value = "shop_notice")
    private String shopNotice;

    /**
     * 售卖模式 ： 1-菜单售卖模式 2-商品售卖模式
     */
    @TableField(value = "sell_type")
    private Integer sellType;

    /**
     * 店铺门牌号
     */
    @TableField(value = "door_number")
    private String doorNumber;

    /**
     * 店铺,自提点地址图片(1.1.0)
     */
    @TableField(value = "address_picture")
    private String addressPicture;

    /**
     * 商户服务:1;外卖小程序,2:食堂美食城（json字符串）【1.2.0】
     */
    @TableField(value = "shop_service")
    private String shopService;

    /**
     * 是否可修改商品价格：0：否，1：是 [1.2.0]
     */
    @TableField(value = "change_good_price")
    private Boolean changeGoodPrice;

    /**
     * V1.5.0 配送类型： 1-自配送、2-达达配送
     */
    @TableField(value = "delivery_type")
    private int deliveryType;
}