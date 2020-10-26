package com.meiyuan.catering.allinpay.model.param.member;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import com.meiyuan.catering.allinpay.utils.DecryptDeserializer;
import com.meiyuan.catering.allinpay.utils.EncryptSerializer;
import lombok.Builder;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 11:33
 * @since v1.1.0
 */
@Data
@Builder
public class BindBankCardParams extends AllinPayBaseServiceParams {

    /**
     * 类型: String
     * 是否必填：是
     * 描述: 流水号
     * 示例值: 请求绑定银行卡接口返回
     */
    private String tranceNum;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 申请时间
     * 示例值: 请求绑定银行卡接口返回
     */
    private String transDate;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 银行预留手机
     * 示例值: xx
     */
    private String phone;
    /**
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述: 有效期
     * 示例值: s格式为月年；如0321，2位月2位年，AES加密,使用万鉴通4要素绑信用卡可以不上送此字段
     */
    @JSONField(serializeUsing = EncryptSerializer.class, deserializeUsing = DecryptDeserializer.class)
    private String validate;
    /**
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述: CVV2
     * 示例值: 3位数字
     */
    @JSONField(serializeUsing = EncryptSerializer.class, deserializeUsing = DecryptDeserializer.class)
    private String cvv2;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 短信验证码
     * 示例值: xxx
     */
    private String verificationCode;


}
