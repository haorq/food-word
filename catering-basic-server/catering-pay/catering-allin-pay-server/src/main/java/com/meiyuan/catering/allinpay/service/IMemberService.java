package com.meiyuan.catering.allinpay.service;


import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import com.meiyuan.catering.allinpay.model.param.member.*;
import com.meiyuan.catering.allinpay.model.result.member.*;

public interface IMemberService extends BaseAllinPayService {

    /**
     * 创建会员
     *
     * <ul>
     * <li>1、该接口用于为业务端各类交易角色创建用户，支持个人会员、企业会员。</li>
     * <li>2、业务端通过唯一标识（bizUserId）创建会员，接口返回通商云会员唯一标识（userId），两者一一对应，
     * 在后 续的接口调用中一般通过业务端唯一标识（bizUserId）来标识用户身份。</li>
     * </ul>
     *
     * @param param 创建会员参数
     * @return com.meiyuan.marsh.payment.allinpay.yunst.model.result.member.CreateMemberResult
     * @author zengzhangni
     * @date 2020/9/24 17:01
     * @since v1.4.0
     */
    CreateMemberResult createMember(CreateMemberParams param);


    /**
     * 发送短信验证码
     * <ul>
     * <li>1、绑定手机前调用此接口（verificationCodeType=9[绑定手机]），系统将向待绑定手机号码发送动态短信验证码。</li>
     * <li>2、测试环境下，短信验证码从管理后台中查看。</li>
     * <li>3、生产环境下，短信验证码会真实发送到用户待绑定手机，管理后台不允许查看。</li>
     * <li>4、解绑手机前调用此接口（verificationCodeType=6[解绑手机]），系统将向待解绑手机号码发送动态短信验证码。</li>
     * </ul>
     *
     * @param params
     * @return com.meiyuan.marsh.payment.allinpay.yunst.model.result.member.SendVerificationCodeResult
     * @author zengzhangni
     * @date 2020/9/24 17:11
     * @since v1.4.0
     */
    SendVerificationCodeResult sendVerificationCode(SendVerificationCodeParams params);

    /**
     * 绑定手机
     * 本接口自带验证短信验证码的逻辑，因此调用本接口实现绑定会员手机时，
     * 应先调用：发送短信 验证码接口（sendVerificationCode），且请求参数验证码类型为“绑定手机”。
     * <ul>
     * <li>
     * 1、个人/企业会员绑定手机才能创建订单（托管代付订单除外），如未绑定手机但已实名仅可作为消费、分账的
     * 收款方，以及托管代收中的目标收款人（recieverList.bizUserId）。
     * </li>
     * <li>
     * 2、个人会员创建会员后即可绑定手机，与是否实名认证无关。
     * </li>
     * <li>
     * 3、企业会员需审核通过后才能绑定手机。
     * </li>
     * </ul>
     *
     * @param params
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:50
     * @since v1.4.0
     */
    BindPhoneResult bindPhone(BindPhoneParams params);

    /**
     * 会员电子协议签约
     * <ul>
     * <li>（1） 以前台 H5 页面形式进行请求，为平台端的个人会员及企业会员通过页面方式签订三方协议（会员、平 台、通联）。</li>
     * <li>（2） 签约返回字段"ContractNo 会员电子协议编号"，商户端需保存。</li>
     * <li>（3） 签约成功提供后台异步通知；签约失败，页面提示失败原因，不提供异步通知。</li>
     * <li>（4） 个人会员电子协议签约前须完成实名认证。</li>
     * <li>（5） 企业会员电子协议签约前须完成设置企业信息，且企业信息审核成功。</li>
     * <li>（6） 未签约会员将控制提现功能。</li>
     * </ul>
     * <b>
     * 注：因商户使用微信小程序进行会员电子协议签约时，需提供商户小程序文件上传至通商云服务器，同时在微信端绑定通商云域名地址，
     * 但微信端限制一个域名最多绑定 20 个小程序，目前通商云 2.0 域名已超过限制，
     * 后续使用到小程序支付的商户，需向总部研发提交申请，总部研发上传小程序文件和分配新域名，
     * 商户使用新域名替换现有域名（fintech.allinpay.com）发起会议电子协议签约，
     * 没有用到小程序支付的商户发起会员电子协议签 约仍使用现有域名；
     * <b/>
     *
     * @param params
     * @return String
     * @author zengzhangni
     * @date 2020/9/24 17:50
     * @since v1.4.0
     */
    String signContract(SignContractParams params);

    /**
     * 描述:个人实名认证
     * <p>
     * 1.绑定银行卡前需先进行实名认证。
     * 2.个人会员创建会员后即可实名认证，与是否绑定手机无关。
     * 3.实名认证是去公安网验证这个人是真实存在的。
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:50
     * @since v1.4.0
     */
    SetRealNameResult setRealName(SetRealNameParams params);

    /**
     * 设置企业信息
     * <b>1、企业会员必须审核成功，并且绑定手机后，才能进行订单操作。</b>
     * <ul> 企业会员审核认证流程：
     * <li>
     * 1) 发送企业营业执照、开户证明（可选）、法人身份证件正反面扫描件通过 FTP 提供通联，
     * 具体 FTP 路径及 账户密码通联将与平台生产参数证书一同提供。（测试环境无需发送）</li>
     * <li>
     * 2) 通商云后台审核（通联分公司运营）。
     * </li>
     * <li>
     * 3) 调用设置企业会员信息后，企业认证需进行线上认证：通商云实时完成企业信息核验，需平台承担企业认证 手续费。
     * 通商云实时完成企业信息核验，通过响应及企业信息审核结果通知获取 result-审核结果。
     * </li>
     * <li>
     * 4) 测试环境不支持线上认证，需通过管理后台菜单“企业会员管理”审核。
     * </li>
     * <li>
     * 5) 企业工商要素验证：企业名称，法人姓名，法人证件号、认证类型，统一信用代码，工商注册号，纳税人识 别号，组织机构代码
     * </li>
     * </ul>
     *
     * @param params
     * @return SetCompanyResult
     * @author zengzhangni
     * @date 2020/9/24 17:50
     * @since v1.4.0
     */
    SetCompanyResult setCompanyInfo(SetCompanyInfoParams params);

    /**
     * 描述:获取会员信息
     * <p>
     * 该接口支持查询个人会员、企业会员。
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:51
     * @since v1.4.0
     */
    MemberInfoResult getMemberInfo(AllinPayBaseServiceParams params);

    /**
     * 描述:查询卡bin
     * <p>
     * 该接口用于绑定银行卡操作中根据用户输入的银行卡卡号查询卡名、卡种类型、开户银行等信息，
     * 业务端可根据开户银行名称动态显示银行LOGO；如果绑定贷记卡的，可根据接口返回的卡类型加载CVV2、
     * 有效期等表单元素共用户填写。另外调用绑卡接口时可能还需上送该接口返回的部分信息。
     * 注：测试环境下，只支持622848、622849开头的19位银行卡（后13位可以随便指定）。
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:51
     * @since v1.4.0
     */
    GetBankCardBinResult getBankCardBin(GetBankCardBinParams params);

    /**
     * 请求绑定银行卡
     * <ul>
     * <li>1. 个人用户必须先完成【个人实名认证】才能绑定银行卡，且接口请求参数（姓名和证件号码）必须与实名信 息一致，验证与实名信息是同人银行卡，且银行卡真实有效。</li>
     * <li>2. 个人用户默认允许绑定多张银行卡，具体配置信息可与通商云业务人员确认。</li>
     * <li>4. 企业用户最多只允许绑定一张法人的个人银行卡，支持通过【解绑绑定银行卡】接口解绑。</li>
     * <li>5. 企业用户如需提现到个人银行账户，可调用此接口绑定银行卡。</li>
     * <li>6. 企业用户绑定个人银行卡前，必须设置企业信息必须审核通过。</li>
     * </ul>
     * <p>
     * 绑定银行卡流程及相关接口：
     * <ul>
     * <li>1) 调用请求绑定银行卡接口（applyBindBankCard），此接口会自动发送短信验证码；</li>
     * <li>2) 调用确认绑定银行卡接口（bindBankCard），通联通账户实名验证（三要素）、通联通账户实名验证(四要 素)、银行卡四要素验证（万鉴通，全部银行）无需调用此接口。</li>
     * </ul>
     * <b>
     * 注：
     * 1) 测试环境下，只支持 622848、622849 开头的 19 位银行卡（后 13 位可以随便指定）。
     * 2) 测试环境下，151,152,153,181,182,183、188 开头的手机号有特殊用途，可能造成绑卡失败，不建议使用。 151、152、153、181、182、183、188 开头。
     * </b>
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:51
     * @since v1.4.0
     */
    ApplyBindBankCardResult applyBindBankCard(ApplyBindBankCardParams params);

    /**
     * 确认绑定银行卡
     * <ul>
     * <li>1. 调用本接口实现四要素、实名付绑定银行卡时，应先调用：请求绑定银行卡接口（applyBindBankCard）；</li>
     * <li>2. 本接口自带验证短信验证码的逻辑。</li>
     * </ul>
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:51
     * @since v1.4.0
     */
    BindBankCardResult bindBankCard(BindBankCardParams params);

    /**
     * 查询绑定银行卡
     * <b>该接口用于查询用户已绑定的某张银行卡，或已绑定的全部银行卡，响应报文支持返回多条记录。</b>
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:51
     * @since v1.4.0
     */
    QueryBankCardResult queryBankCard(QueryBankCardParams params);

    /**
     * 解绑绑定银行卡
     * <ul>
     * <li>1. 如需解绑银行卡，直接调用该接口，无需调用其它接口进行确认。</li>
     * <li>2. 适用于四要素+短信绑定、实名付绑定、账户实名验证(四要素)、通联通快捷、收银宝快捷、万鉴通四要素 绑定的银行卡。</li>
     * <li>3. 如果会员账户余额不为零，最后一张银行卡仍可解绑，通商云无限制。</li>
     * <li>4. 支持解绑个人会员绑定的银行卡和企业会员绑定的法人银行卡。</li>
     * <li>5. 安全卡不能解绑，如需更换安全卡请联系通商云业务人员通过线下流程进行变更</li>
     * </ul>
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:51
     * @since v1.4.0
     */
    UnbindBankCardResult unbindBankCard(UnbindBankCardParams params);

    /**
     * 锁定会员
     * <b>在业务端用户存在账户安全风险等异常情况，需要限制交易时使用，会员被锁定后将无法创建订单，也不能收款。
     * 除解锁会员之外，其它接口都将返回错误信息“会员已锁定”。</b>
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:52
     * @since v1.4.0
     */
    LockMemberResult lockMember(LockMemberParams params);

    /**
     * 描述:解锁会员
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:52
     * @since v1.4.0
     */
    UnlockMemberResult unlockMember(UnlockMemberParams params);

    /**
     * 描述:设置支付密码[密码验证版]
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:52
     * @since v1.4.0
     */
    void setPayPwd();

    /**
     * 描述:修改支付密码[密码验证版]
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:53
     * @since v1.4.0
     */
    void updatePayPwd();

    /**
     * 描述:重置支付密码[密码验证版]
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:53
     * @since v1.4.0
     */
    void resetPayPwd();

    /**
     * 描述:修改绑定手机[密码验证版]
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:53
     * @since v1.4.0
     */
    void updatePhoneByPayPwd();

    /**
     * 会员绑定支付账户用户标识
     * <ul>
     * <li>
     * （1）适用个人会员注册后，无法绑定手机号的业务场景使用，通过此接口绑定支付账户用户标识后，可进 行订单操作，仅限于不使用余额功能的平台使用。
     * </li>
     * <li>
     * （2）一个会员可绑定多个支付账户用户标识，调多次接口；
     * </li>
     * <li>
     * （3）会员进行微信公众号支付、微信小程序支付、支付宝生活号支付时，支付方式中的“acct”需与会员 绑定的“acct”一致；
     * </li>
     * <li>
     * （4）会员如需使用通商云短信验证交易，需通过通商云验证短信验证码更新手机号【修改绑定手机（短信 验证码确认）】、【修改绑定手机（密码验证版）】，字段“oldPhone 原手机”赋值：88888888888
     * </li>
     * <li>
     * （5）未通过【绑定手机】接口进行过手机号绑定的会员，【设置支付密码（密码验证版）】【重置支付密 码（密码验证版）】字段“Phone 原手机”赋值：88888888888
     * </li>
     * </ul>
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:53
     * @since v1.4.0
     */
    ApplyBindAcctResult applyBindAcct(ApplyBindAcctParams params);

    /**
     * 描述:解绑手机[验证原手机短信验证码]
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/9/24 17:53
     * @since v1.4.0
     */
    UnbindPhoneResult unbindPhone(UnbindPhoneParams params);

    /**
     * 会员电子协议签约查询
     * <ul>
     * <li>当会员已签约，可调用【会员电子协议签约查询】接口，查询具体协议内容，点击“确定“按钮，进入到商 户上送的 jumpUrl 页面;</li>
     * <li>
     * 协议查询请求地址
     * 测试环境网关地址： http://116.228.64.55:6900/yungateway/member/signContractQuery.html
     * 生产环境网关地址： https://fintech.allinpay.com/yungateway/member/signContractQuery.html
     * </li>
     * </ul>
     *
     * @param params
     * @return string
     * @author zengzhangni
     * @date 2020/9/24 17:54
     * @since v1.4.0
     */
    String signContractQuery(SignContractQueryParams params);

    /**
     * 描述:注： 功能接入参考《【通商云】收银宝当面付及 POS 订单支付使用指南》
     * 1、收银宝当面付及 POS 订单支付功能 功能接入参考《【通商云】收银宝当面付及 POS 订单支付使用指南》
     * 1.1-支持个人和企业会员绑定查询收银宝当面付二维码编号及收银宝 POS 终端号；
     * 1.2-仅需使用收银宝当面付的标准支付模式及收银宝 POS 订单支付（不查订单）；
     * 1.3-一个会员可绑定多个终端号，调多次接口；一个终端号仅可绑定一个会员。
     * <p>
     * 2、收银宝集团商户支付功能
     * 2.1-收款商户会员编号（ bizUserId）需与收银宝子商户号（vspCusid）绑定，
     * 一个会员可绑定多个收银宝 子商户号，调多次接口。
     * 2.2-消费/托收等入金订单支付时，支付方式中
     * vspCusid-收银宝子商户号必须与订单收款人绑定的 vspCusid-收银宝子商户号一致。
     *
     * @param params
     * @return com.meiyuan.catering.allinpay.model.result.member.VspTermidServiceResult
     * @author zengzhangni
     * @date 2020/10/17 11:40
     * @since v1.5.0
     */
    VspTermidServiceResult vspTermidService(VspTermidServiceParams params);
}
