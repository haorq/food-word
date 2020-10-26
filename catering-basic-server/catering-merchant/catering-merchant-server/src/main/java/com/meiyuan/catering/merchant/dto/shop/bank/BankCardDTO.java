package com.meiyuan.catering.merchant.dto.shop.bank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 1. 个人用户必须先完成【个人实名认证】才能绑定银行卡，且接口请求参数（姓名和证件号码）必须与实名信 息一致，
 * 验证与实名信息是同人银行卡，且银行卡真实有效。
 * 2. 个人用户默认允许绑定多张银行卡，具体配置信息可与通商云业务人员确认。
 * 4. 企业用户最多只允许绑定一张法人的个人银行卡，支持通过【解绑绑定银行卡】接口解绑。
 *
 * @author zengzhangni
 * @date 2020/9/29 18:03
 * @since v1.1.0
 */
@Data
public class BankCardDTO {
    /**绑定银行卡必传*/
    @ApiModelProperty(value = "会员id", hidden = true)
    public String bizUserId;
    @ApiModelProperty(value = "实名认证真实姓名", hidden = true)
    private String name;
    @ApiModelProperty(value = "身份证", hidden = true)
    private String identityNo;

    @ApiModelProperty(value ="银行卡号",required = true)
    @NotBlank(message = "银行卡号不能为空")
    private String cardNo;
    @ApiModelProperty(value = "银行预留手机",required = true)
    private String phone;
    @ApiModelProperty(value = "验证码", required = true)
    private String code;

    /**获取验证码返回*/
    @ApiModelProperty(value = "流水号 绑卡方式6、7返回  确认绑卡的请求参数", required = true)
    private String tranceNum;
    @ApiModelProperty(value = "支付行号", hidden = true)
    private String unionBank;
    @ApiModelProperty(value = "银行名称", required = true)
    private String bankName;
}
