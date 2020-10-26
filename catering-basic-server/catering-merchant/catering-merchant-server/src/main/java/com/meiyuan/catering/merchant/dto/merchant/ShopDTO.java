package com.meiyuan.catering.merchant.dto.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/5/19 0019 11:44
 * @Description 简单描述 : 店铺所有信息DTO
 * @Since version-1.1.0
 */
@Data
@ApiModel("店铺所有信息DTO")
public class ShopDTO {
    @ApiModelProperty("id")
    private Long id;

    /**
     * 商户id
     */
    @ApiModelProperty("商户id")
    private Long merchantId;
    /**
     * 店铺编码
     */
    @ApiModelProperty("店铺编码")
    private String shopCode;
    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String shopName;
    /**
     * 门店联系电话
     */
    @ApiModelProperty("门店联系电话")
    private String shopPhone;
    /**
     * 门店LOGO图片
     */
    @ApiModelProperty("门店LOGO图片")
    private String doorHeadPicture;
    /**
     * 店面图片
     */
    @ApiModelProperty("店面图片")
    private String shopPicture;

    /**
     * 门店登陆密码
     */
    @TableField(value = "门店登陆密码")
    private String password;
    /**
     * 门店联系人
     */
    @ApiModelProperty("门店联系人")
    private String primaryPersonName;

    /**
     * 门店注册电话（门店登录账号）
     */
    @ApiModelProperty("门店注册电话（门店登录账号）")
    private String registerPhone;
    /**
     * 身份证正面
     */
    @ApiModelProperty("身份证正面")
    private String idCardPositive;
    /**
     * 身份证反面
     */
    @ApiModelProperty("身份证反面")
    private String idCardBack;

    /**
     * 营业执照
     */
    @ApiModelProperty("营业执照")
    private String businessLicense;
    /**
     * 食品经营许可证
     */
    @ApiModelProperty("食品经营许可证")
    private String foodBusinessLicense;
    /**
     * 门店来源：1- 商户申请 2-平台添加
     */
    @ApiModelProperty("门店来源：1- 商户申请 2-平台添加")
    private Integer shopSource;
    /**
     * 类型：是否绑定自身:0- 不是 1-是
     */
    @ApiModelProperty("类型：是否绑定自身:0- 不是 1-是")
    private Boolean isPickupPoint;
    /**
     * 门店保证金
     */
    @ApiModelProperty("门店保证金")
    private BigDecimal earnestMoney;

    /**
     * 省
     */
    @ApiModelProperty("省")
    private String addressProvinceCode;
    /**
     * 市
     */
    @ApiModelProperty("市")
    private String addressCityCode;
    /**
     * 区
     */
    @ApiModelProperty("区")
    private String addressAreaCode;
    /**
     * 地址省
     */
    @ApiModelProperty("地址省")
    private String addressProvince;
    /**
     * 地址市
     */
    @ApiModelProperty("id")
    private String addressCity;
    /**
     * 地址区
     */
    @ApiModelProperty("地址区")
    private String addressArea;
    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String addressDetail;
    /**
     * 完整地址
     */
    @ApiModelProperty("完整地址")
    private String addressFull;
    /**
     * 经纬度
     */
    @ApiModelProperty("经纬度")
    private String mapCoordinate;
    /**
     * 门店类型：1-门店 2-门店兼自提点 3-自提点
     */
    @ApiModelProperty("门店类型：1-门店 2-门店兼自提点 3-自提点")
    private Integer shopType;
    /**
     * 营业开始时间
     */
    @ApiModelProperty("营业开始时间")
    private String openingTime;
    /**
     * 营业结束时间
     */
    @ApiModelProperty("营业结束时间")
    private String closingTime;
    /**
     * 营业状态：1-营业 2-打样
     */
    @ApiModelProperty("营业状态：1-营业 2-打样")
    private Integer businessStatus;

    /**
     * 店铺状态:1：启用，2：禁用
     */
    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;
    /**
     * 0-否 1-是
     */
    @ApiModelProperty("0-否 1-是")
    private Boolean del;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    /**
     * 最近一次登录IP地址
     */
    @ApiModelProperty("最近一次登录IP地址")
    private String lastLoginIp;
    /**
     * 最近一次登录时间
     */
    @ApiModelProperty("最近一次登录时间")
    private LocalDateTime lastLoginTime;

    /**
     * 门店公告
     */
    @ApiModelProperty("门店公告")
    private String shopNotice;

    /**
     * 售卖模式 ： 1-菜单售卖模式 2-商品售卖模式
     */
    @ApiModelProperty("售卖模式 ： 1-菜单售卖模式 2-商品售卖模式")
    private Integer sellType;

    /**
     * 店铺门牌号
     */
    @ApiModelProperty("店铺门牌号")
    private String doorNumber;

    @ApiModelProperty(value = "店铺,自提点地址图片(1.1.0)")
    private List<String> addressPictureList;
    @ApiModelProperty(value = "店铺,自提点地址图片(1.1.0)")
    private List<ShopAddressPictureDTO> addressPictureDTOS;

    public Long  getShopId(){
        return this.id;
    }
}
