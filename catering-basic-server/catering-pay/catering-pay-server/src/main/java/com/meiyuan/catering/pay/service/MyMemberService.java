package com.meiyuan.catering.pay.service;

import com.meiyuan.catering.allinpay.enums.service.AcctTypeEnums;
import com.meiyuan.catering.allinpay.enums.service.member.MemberTypeEnums;
import com.meiyuan.catering.allinpay.enums.service.member.VerificationCodeTypeEnums;
import com.meiyuan.catering.allinpay.model.result.member.*;
import com.meiyuan.catering.pay.dto.member.ApplyBindBankCardDTO;
import com.meiyuan.catering.pay.dto.member.BindBankCardDTO;

/**
 * 描述: 会员服务
 *
 * @author zengzhangni
 * @date 2020/9/29 14:41
 * @since v1.5.0
 */
public interface MyMemberService {


    /**
     * 描述:注册个人会员
     *
     * @param userId 用户id
     * @param openId 支付标识
     * @return Boolean
     * @author zengzhangni
     * @date 2020/9/29 15:51
     * @since v1.5.0
     */
    Boolean registerPersonalMember(Long userId, String openId);


    /**
     * 描述: 创建会员
     *
     * @param id
     * @param memberType (2L, "企业会员"),(3L, "个人会员");
     * @return void
     * @author zengzhangni
     * @date 2020/9/29 15:38
     * @since v1.5.0
     */
    CreateMemberResult createMember(Long id, Long memberType);

    /**
     * 描述:创建个人会员
     *
     * @param id
     * @return com.meiyuan.catering.allinpay.model.result.member.CreateMemberResult
     * @author zengzhangni
     * @date 2020/9/29 15:52
     * @since v1.5.0
     */
    default CreateMemberResult createPersonalMember(Long id) {
        return createMember(id, MemberTypeEnums.PERSONAL_MEMBER.getType());
    }


    /**
     * 描述:绑定支付标识
     *
     * @param userId   用户id
     * @param acctType 绑定类型 "weChatMiniProgram", "微信小程序"
     * @param acct     支付标识  openId
     * @return com.meiyuan.catering.allinpay.model.result.member.ApplyBindAcctResult
     * @author zengzhangni
     * @date 2020/9/29 15:52
     * @since v1.5.0
     */
    ApplyBindAcctResult applyBindAcct(Long userId, AcctTypeEnums acctType, String acct);


    /**
     * 描述:绑定小程序支付标识
     *
     * @param userId 用户id
     * @param openId openId
     * @return com.meiyuan.catering.allinpay.model.result.member.ApplyBindAcctResult
     * @author zengzhangni
     * @date 2020/9/29 15:54
     * @since v1.5.0
     */
    default ApplyBindAcctResult applyBindMiniAcct(Long userId, String openId) {
        return applyBindAcct(userId, AcctTypeEnums.WECHAT_MINI_PROGRAM, openId);
    }


    /**
     * 描述:个人实名认证
     *
     * @param id         用户id 或 门店id
     * @param name       姓名
     * @param identityNo 身份证号
     * @return com.meiyuan.catering.allinpay.model.result.member.SetRealNameResult
     * @author zengzhangni
     * @date 2020/9/29 16:47
     * @since v1.5.0
     */
    SetRealNameResult setRealName(Long id, String name, String identityNo);

    /**
     * 描述:会员电子协议签约
     *
     * @param id 用户id 或 门店id
     * @return java.lang.String h5签约链接
     * @author zengzhangni
     * @date 2020/9/29 16:56
     * @since v1.5.0
     */
    String signContract(Long id);

    /**
     * 描述:会员电子协议签约查询
     *
     * @param id
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/10/12 13:52
     * @since v1.5.0
     */
    String signContractQuery(Long id);


    /**
     * 描述:请求绑定银行卡
     *
     * @param dto
     * @return com.meiyuan.catering.allinpay.model.result.member.ApplyBindBankCardResult
     * @author zengzhangni
     * @date 2020/9/29 18:18
     * @since v1.5.0
     */
    ApplyBindBankCardResult applyBindBankCard(ApplyBindBankCardDTO dto);


    /**
     * 描述:确认绑定银行卡
     *
     * @param dto
     * @return com.meiyuan.catering.allinpay.model.result.member.BindBankCardResult
     * @author zengzhangni
     * @date 2020/9/29 18:22
     * @since v1.5.0
     */
    BindBankCardResult bindBankCard(BindBankCardDTO dto);

    /**
     * 描述:绑定手机/解绑手机号,发送验证码
     *
     * @param shopId
     * @param phone
     * @param codeTypeEnums
     * @return com.meiyuan.catering.allinpay.model.result.member.SendVerificationCodeResult
     * @author zengzhangni
     * @date 2020/10/10 17:08
     * @since v1.5.0
     */
    SendVerificationCodeResult sendVerificationCode(Long shopId, String phone, VerificationCodeTypeEnums codeTypeEnums);

    /**
     * 描述:绑定手机号
     *
     * @param shopId
     * @param phone
     * @param verificationCode
     * @return com.meiyuan.catering.allinpay.model.result.member.BindPhoneResult
     * @author zengzhangni
     * @date 2020/10/13 10:22
     * @since v1.5.0
     */
    BindPhoneResult bindPhone(Long shopId, String phone, String verificationCode);

    /**
     * 描述:查询会员信息
     *
     * @param id
     * @return com.meiyuan.catering.allinpay.model.result.member.MemberInfoResult
     * @author zengzhangni
     * @date 2020/10/13 10:25
     * @since v1.5.0
     */
    MemberInfoResult getMemberInfo(Long id);

    /**
     * 描述:会员收银宝渠道商户信息及终端信息绑定
     *
     * @param id
     * @return VspTermidServiceResult
     * @author zengzhangni
     * @date 2020/10/17 11:43
     * @since v1.5.0
     */
    VspTermidServiceResult bindVspTermid(Long id);

}
