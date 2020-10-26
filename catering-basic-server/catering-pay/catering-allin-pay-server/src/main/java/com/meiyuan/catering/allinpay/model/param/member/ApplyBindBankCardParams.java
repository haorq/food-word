package com.meiyuan.catering.allinpay.model.param.member;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.allinpay.utils.DecryptDeserializer;
import com.meiyuan.catering.allinpay.utils.EncryptSerializer;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 11:33
 * @since v1.1.0
 */
@Data
@Builder
public class ApplyBindBankCardParams extends AllinPayBaseServiceParams {
    /**
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述:银行卡号
     */
    @JSONField(serializeUsing = EncryptSerializer.class, deserializeUsing = DecryptDeserializer.class)
    private String cardNo;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 银行预留手机 绑卡方式1-通联通三要素绑卡，手机号可不上送 其他绑卡方式必须上送
     * 示例值:
     */
    private String phone;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 姓名
     * 示例值: 如果是企业会员，请填写法人姓名
     */
    private String name;
    /**
     * 类型: Long
     * 是否必填：否
     * 描述: 绑卡方式
     * 示例值: 默认绑卡方式-7
     */
    private Long cardCheck;
    /**
     * 类型: Long
     * 是否必填：是
     * 描述: 证件类型
     * 示例值: 目前只支持身份证 1
     */
    private Long identityType;
    /**
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述: 证件号码
     * 示例值: xx
     */
    @JSONField(serializeUsing = EncryptSerializer.class, deserializeUsing = DecryptDeserializer.class)
    private String identityNo;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 有效期
     * 示例值: 格式为月年；如0321，2位月2位年，AES加密 详情。使用万鉴通4要素绑信用卡可以不上送此字段
     */
    @JSONField(serializeUsing = EncryptSerializer.class, deserializeUsing = DecryptDeserializer.class)
    private String validate;
    /**
     * 类型: String
     * 是否必填：否
     * 是否加密：AES加密
     * 描述: CVV2
     * 示例值: 3位数字，AES加密 详情使用万鉴通4要素绑信用卡可以不上送此字段
     */
    @JSONField(serializeUsing = EncryptSerializer.class, deserializeUsing = DecryptDeserializer.class)
    private String cvv2;
    /**
     * 类型: Boolean
     * 是否必填：否
     * 描述: 是否安全卡
     * 示例值: 信用卡时不能填写：true:设置为安全卡，false:不设置。默认为false
     */
    private Boolean isSafeCard;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 支付行号
     * 示例值: 12位数字
     */
    private String unionBank;


}
