package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author MeiTao
 * @Description 自提点添加参数DTO
 * @Date  2020/3/12 0012 16:20
 */
@Data
@ApiModel("自提点添加参数DTO")
public class MerchantsPickupAddressDTO extends IdEntity {
    @ApiModelProperty("商户id【1.2.0】")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty("自提点名称")
    private String shopName;
    @ApiModelProperty("负责人姓名")
    private String primaryPersonName;
    @ApiModelProperty("负责人手机号")
    private String registerPhone;

    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("完整地址")
    private String addressFull;
    @ApiModelProperty("门牌号")
    private String doorNumber;
    @ApiModelProperty("经纬度")
    private String mapCoordinate;

    @ApiModelProperty("登录密码")
    private String password;
    @ApiModelProperty("自提点状态:1：启用，2：禁用")
    private Integer shopStatus;

    @ApiModelProperty("营业执照")
    private String businessLicense;
    @ApiModelProperty("食品经营许可证")
    private String foodBusinessLicense;

    @ApiModelProperty("身份证正面【1.1.0】")
    private String idCardPositive;
    @ApiModelProperty("身份证反面【1.1.0】")
    private String idCardBack;

    @ApiModelProperty("店铺保证金【1.1.0】")
    private BigDecimal earnestMoney;
}
