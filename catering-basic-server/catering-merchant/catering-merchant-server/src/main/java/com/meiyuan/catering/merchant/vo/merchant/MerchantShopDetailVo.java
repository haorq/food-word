package com.meiyuan.catering.merchant.vo.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 商户店铺列表查询VO
 * @Date  2020/3/12 0012 10:32
 */
@Data
@ApiModel("商户店铺详情查询VO")
public class MerchantShopDetailVo {
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("店铺编码")
    private String shopCode;
    @ApiModelProperty("店铺类型：1-店铺 2-店铺兼自提点 3-自提点")
    private Integer shopType;
    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("门牌号")
    private String doorNumber;
    @ApiModelProperty("完整地址")
    private String addressFull;
    @ApiModelProperty("店铺状态:1-营业 2-打样")
    private Integer businessStatus;
    @ApiModelProperty("店铺来源：1- 商户申请 2-平台添加")
    private Integer shopSource;
    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;

    @ApiModelProperty("店铺保证金")
    private BigDecimal earnestMoney;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("商家联系人")
    private String primaryPersonName;
    @ApiModelProperty("注册电话/商家联系人电话")
    private String registerPhone;


    @ApiModelProperty("售卖模式 ： 1-菜单售卖模式 2-商品售卖模式")
    private Integer sellType;

    @ApiModelProperty("是否是自提点：0- 不是 1-是")
    private Integer isPickupPoint;

    @ApiModelProperty("自动接单:1-自动 2-不自动")
    private Integer autoReceipt;

    @ApiModelProperty("配送对象:1-企业 2-个人 3-全部")
    private Integer deliveryObject;

    @ApiModelProperty("配送范围(米)")
    private Integer deliveryRange;

    @ApiModelProperty("业务支持：1：仅配送，2：仅自提，3：全部")
    private String businessSupport;

    @ApiModelProperty("店铺标签")
    private List<MerchantShopTagsVo> merchantShopTags;

    @ApiModelProperty("店铺地址图片【1.1.0】")
    private List<String> addressPictureList;
    @ApiModelProperty("店铺地址图片")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private String addressPicture;
    @ApiModelProperty("类型：1--商家，2--自提点，3--商家兼自提点[1.1.0]")
    private Integer type;


    @ApiModelProperty("门店服务【1.2.0】")
    private String shopService;

    @ApiModelProperty("店铺logo")
    private String doorHeadPicture;
    @ApiModelProperty("门店电话")
    private String shopPhone;

    @ApiModelProperty("营业开始时间")
    private String openingTime;
    @ApiModelProperty("营业结束时间")
    private String closingTime;
    @ApiModelProperty("经纬度")
    private String mapCoordinate;

    @ApiModelProperty("门店公告【1.2.0】")
    private String shopNotice;

    @ApiModelProperty("是否可修改商品价格：0：否，1：是【1.2.0】")
    private Boolean changeGoodPrice;

    @ApiModelProperty("手持身份证【1.5.0】")
    private String idCardWithhand;
    @ApiModelProperty("开户银行【1.5.0】")
    private String bankName;
    @ApiModelProperty("开户支行【1.5.0】")
    private String bankBranch;
    @ApiModelProperty("开户银行卡号【1.5.0】")
    private String bankCardNumber;
    @ApiModelProperty("身份证正面")
    private String idCardPositive;
    @ApiModelProperty("身份证反面")
    private String idCardBack;

    @ApiModelProperty("营业执照")
    private String businessLicense;
    @ApiModelProperty("食品经营许可证")
    private String foodBusinessLicense;
    @ApiModelProperty("配送类型： 1-自配送、2-达达配送【V1.5.0】")
    private int deliveryType;
}
