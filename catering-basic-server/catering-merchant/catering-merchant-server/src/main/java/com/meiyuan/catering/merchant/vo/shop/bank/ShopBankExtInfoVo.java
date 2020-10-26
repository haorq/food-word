package com.meiyuan.catering.merchant.vo.shop.bank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/10/10 16:10
 * @description 店铺扩展表信息VO
 **/

@Data
@ApiModel(value = "店铺扩展表信息VO")
public class ShopBankExtInfoVo {

    @ApiModelProperty(value = "用户通联注册真实姓名")
    private String userName;
    @ApiModelProperty(value = "用户通联注册真实身份证号")
    private String idCard;
    @ApiModelProperty(value = "通联会员/银行卡属性: 0-个人银行卡、1-企业对公账户")
    private Long bankCardPro;
    @ApiModelProperty(value = "开户银行")
    private String bankName;
    @ApiModelProperty(value = "开户支行")
    private String bankBranch;
    @ApiModelProperty(value = "开户行卡号")
    private String bankCardNumber;

}
