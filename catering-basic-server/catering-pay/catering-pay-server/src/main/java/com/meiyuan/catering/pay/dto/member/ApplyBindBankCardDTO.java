package com.meiyuan.catering.pay.dto.member;

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
public class ApplyBindBankCardDTO {


    @ApiModelProperty("银行卡号")
    @NotBlank(message = "银行卡号不能为空")
    private String cardNo;
    @ApiModelProperty("银行预留手机")
    private String phone;
    @ApiModelProperty(value = "会员id", hidden = true)
    public String bizUserId;
    @ApiModelProperty(value = "姓名 如果是企业会员，请填写法人姓名", hidden = true)
    private String name;
    @ApiModelProperty(value = "身份证", hidden = true)
    private String identityNo;


    @ApiModelProperty(value = "绑卡方式 默认绑卡方式-7", hidden = true)
    private Long cardCheck;
    @ApiModelProperty(value = "证件类型 身份证 1", hidden = true)
    private Long identityType;
    @ApiModelProperty(value = "有效期", hidden = true)
    private String validate;
    @ApiModelProperty(value = "CVV2", hidden = true)
    private String cvv2;
    @ApiModelProperty(value = "是否安全卡", hidden = true)
    private Boolean isSafeCard;
    @ApiModelProperty(value = "支付行号", hidden = true)
    private String unionBank;
}
