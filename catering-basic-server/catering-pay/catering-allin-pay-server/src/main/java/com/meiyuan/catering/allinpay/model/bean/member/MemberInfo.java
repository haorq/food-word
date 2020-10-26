package com.meiyuan.catering.allinpay.model.bean.member;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.allinpay.utils.DecryptDeserializer;
import lombok.Data;

@Data
public class MemberInfo extends MemberBaseInfo {
    /**
     * 姓名
     */
    private String name;
    /**
     * 用户状态
     */
    private Long userState;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 县市
     */
    private String area;
    /**
     * 地址
     */
    private String address;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 身份证号码，AES加密
     */
    @JSONField(deserializeUsing = DecryptDeserializer.class)
    private String identityCardNo;
    /**
     * 是否绑定手机
     */
    private Boolean isPhoneChecked;
    /**
     * 创建时间 yyyy-MM-ddHH:mm:ss
     */
    private String registerTime;

    /**
     * 创建ip
     */
    private String registerIp;
    /**
     * 支付失败次数
     */
    private Long payFailAmount;

    /**
     * 是否进行实名认证
     */
    private Boolean isIdentityChecked;
    /**
     * 实名认证时间，yyyy-MM-ddHH:mm:ss
     */
    private String realNameTime;

    /**
     * 访问终端类型
     */
    private Long source;
    /**
     * 是否已设置支付密码
     */
    private Boolean isSetPayPwd;


}
