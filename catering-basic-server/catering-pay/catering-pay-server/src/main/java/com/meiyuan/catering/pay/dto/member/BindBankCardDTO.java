package com.meiyuan.catering.pay.dto.member;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/29 18:03
 * @since v1.1.0
 */
@Data
public class BindBankCardDTO extends AllinPayBaseServiceParams {
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 流水号
     * 示例值: 请求绑定银行卡接口返回
     */
    @ApiModelProperty(value = "流水号",required = true)
    private String tranceNum;

    /**
     * 类型: String
     * 是否必填：是
     * 描述: 银行预留手机
     * 示例值: xx
     */
    @ApiModelProperty(value = "银行预留手机",required = true)
    private String phone;

    /**
     * 类型: String
     * 是否必填：是
     * 描述: 短信验证码
     * 示例值: xxx
     */
    @ApiModelProperty(value = "短信验证码",required = true)
    private String verificationCode;



    /**
     * 类型: String
     * 是否必填：否
     * 描述: 申请时间
     * 示例值: 请求绑定银行卡接口返回
     */
    @ApiModelProperty(value = "申请时间",hidden = true)
    private String transDate;

    /**
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述: 有效期
     * 示例值: s格式为月年；如0321，2位月2位年，AES加密,使用万鉴通4要素绑信用卡可以不上送此字段
     */
    @ApiModelProperty(value = "有效期",hidden = true)
    private String validate;
    /**
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述: CVV2
     * 示例值: 3位数字
     */
    @ApiModelProperty(value = "CVV2",hidden = true)
    private String cvv2;


}
