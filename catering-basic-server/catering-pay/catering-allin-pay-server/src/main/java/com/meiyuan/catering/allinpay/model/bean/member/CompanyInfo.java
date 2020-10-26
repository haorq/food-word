package com.meiyuan.catering.allinpay.model.bean.member;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.allinpay.utils.EncryptSerializer;
import lombok.Data;

@Data
public class CompanyInfo extends MemberBaseInfo{
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 企业名称
     * 示例值: 企业名称，如有括号，用中文格式（）
     */
    private String companyName;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 企业地址
     * 示例值:
     */
    private String companyAddress;
    /**
     * 类型: Long
     * 是否必填：否
     * 描述: 认证类型
     * 示例值: 1:三证 2:一证  默认 1-三证
     */
    private Long authType;
    /**
     * 类型: String
     * 是否必填：否  认证类型为 1 时必传
     * 描述: 营业执照号（三证）
     * 示例值: 认证类型为 1 时必传
     */
    private String businessLicense;
    /**
     * 类型: String
     * 是否必填：否  认证类型为 1 时必传
     * 描述: 组织机构代码（三证）
     * 示例值: 认证类型为 1 时必传
     */
    private String organizationCode;
    /**
     * 类型: String
     * 是否必填：否  认证类型为 2 时必传
     * 描述: 统一社会信用（一证）
     * 示例值: 认证类型为 2 时必传
     */
    private String uniCredit;
    /**
     * 类型: String
     * 是否必填：否  认证类型为 1 时必传
     * 描述: 税务登记证（三证）
     * 示例值: 认证类型为 1 时必传
     */
    private String taxRegister;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 统一社会信用/营业执照号到期时间 格式：yyyy-MM-dd
     * 示例值: yyyy-MM-dd
     */
    private String expLicense;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 联系电话
     * 示例值:
     */
    private String telephone;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 手机号
     * 示例值:
     */
    private String phone;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 法人姓名
     * 示例值:
     */
    private String legalName;
    /**
     * 类型: Long
     * 是否必填：是
     * 描述: 法人证件类型
     * 示例值:
     */
    private Long identityType;
    /**
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述: 法人证件号码
     * 示例值:
     */
    @JSONField(serializeUsing = EncryptSerializer.class)
    private String legalIds;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 法人手机号码
     * 示例值:
     */
    private String legalPhone;
    /**
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述: 企业对公账户
     * 示例值:
     */
    @JSONField(serializeUsing = EncryptSerializer.class)
    private String accountNo;
    /**
     * 类型: String
     * 是否必填：是
     * 描述:开户银行名称
     * 示例值: 需严格按照银行列表上送， 部分银行支持多种上送方式，选其一上送即可。 注：测试环境仅支持工农中建交。
     */
    private String parentBankName;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 开户行地区代码
     * 示例值: 根据中国地区代码表
     */
    private String bankCityNo;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 开户行支行名称
     * 示例值: 中国工商银行股份有限公司北京樱桃园支行
     */
    private String bankName;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 支付行号
     * 示例值: 12 位数字
     */
    private String unionBank;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 开户行所在省
     * 示例值:开户行所在市必须同时上送
     * 根据中国省市表的“省份”内容填写
     */
    private String province;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 开户行所在市
     * 示例值: 开户行所在省必须同时上送
     * 根据中国省市表的“省份”内容填写
     */
    private String city;

    /**
     * 类型: Long
     * 是否必填：是
     * 描述: 审核状态
     * 示例值:
     */
    private Long status;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 审核时间
     * 示例值:
     */
    private String checkTime;

    /**
     * 类型: String
     * 是否必填：否
     * 描述:审核失败原因
     * 示例值:
     */
    private String failReason;
    /**
     * 类型: Long
     * 是否必填：否
     * 描述: 开户机构类型, 1-华通银行
     * 示例值:
     */
    private Long acctOrgType;

}
