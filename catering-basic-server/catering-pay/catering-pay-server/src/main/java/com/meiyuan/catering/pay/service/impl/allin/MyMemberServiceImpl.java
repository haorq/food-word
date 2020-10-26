package com.meiyuan.catering.pay.service.impl.allin;

import com.meiyuan.catering.allinpay.autoconfigure.AllinPayConfig;
import com.meiyuan.catering.allinpay.enums.service.AcctTypeEnums;
import com.meiyuan.catering.allinpay.enums.service.DeviceTypeEnums;
import com.meiyuan.catering.allinpay.enums.service.OperationTypeEnum;
import com.meiyuan.catering.allinpay.enums.service.member.CardCheckEnums;
import com.meiyuan.catering.allinpay.enums.service.member.IdentityTypeEnums;
import com.meiyuan.catering.allinpay.enums.service.member.VerificationCodeTypeEnums;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import com.meiyuan.catering.allinpay.model.param.member.*;
import com.meiyuan.catering.allinpay.model.result.member.*;
import com.meiyuan.catering.allinpay.service.IMemberService;
import com.meiyuan.catering.pay.dto.member.ApplyBindBankCardDTO;
import com.meiyuan.catering.pay.dto.member.BindBankCardDTO;
import com.meiyuan.catering.pay.enums.NotifyEnum;
import com.meiyuan.catering.pay.service.MyMemberService;
import com.meiyuan.catering.pay.util.PayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述: 会员服务
 *
 * @author zengzhangni
 * @date 2020/9/29 14:41
 * @since v1.5.0
 */
@Service
@Slf4j
public class MyMemberServiceImpl implements MyMemberService {

    @Autowired
    private IMemberService memberService;


    @Override
    public Boolean registerPersonalMember(Long userId, String openId) {

        //创建会员
        CreateMemberResult memberResult = createPersonalMember(userId);
        log.debug("memberResult:{}" + memberResult);
        log.debug("userId:{},memberId:{}", userId, memberResult.getUserId());

        //绑定支付标识(openId)
        ApplyBindAcctResult acctResult = applyBindMiniAcct(userId, openId);
        log.debug("acctResult:{}" + acctResult);
        log.debug("userId:{},acctResult:{}", userId, acctResult.getResult());

        return true;
    }

    @Override
    public CreateMemberResult createMember(Long id, Long memberType) {
        CreateMemberParams params = CreateMemberParams.builder()
                .memberType(memberType)
                .source(DeviceTypeEnums.PC.getType())
                .build();
        params.setBizUserId(id.toString());

        return memberService.createMember(params);
    }

    @Override
    public ApplyBindAcctResult applyBindAcct(Long userId, AcctTypeEnums acctType, String acct) {
        ApplyBindAcctParams acctParams = ApplyBindAcctParams.builder()
                .acctType(acctType.getType())
                .acct(acct)
                .operationType(OperationTypeEnum.SET.getCode())
                .build();
        acctParams.setBizUserId(userId.toString());

        return memberService.applyBindAcct(acctParams);
    }


    @Override
    public SetRealNameResult setRealName(Long id, String name, String identityNo) {
        SetRealNameParams acctParams = SetRealNameParams.builder()
                .isAuth(true)
                .name(name)
                .identityNo(identityNo)
                .identityType(IdentityTypeEnums.ID_CARD.getType())
                .build();
        acctParams.setBizUserId(id.toString());

        return memberService.setRealName(acctParams);
    }

    @Override
    public String signContract(Long id) {
        AllinPayConfig config = memberService.getConfig();

        String backUrl = PayUtil.getNotifyUrl(config.getDomain(), NotifyEnum.SIGN_CONTRACT);
        String jumpUrl = PayUtil.getNotifyUrl(config.getDomainH5(), NotifyEnum.SIGN_CONTRACT_H5);

        SignContractParams acctParams = SignContractParams.builder()
                .backUrl(backUrl)
                .jumpUrl(jumpUrl)
                .source(DeviceTypeEnums.PC.getType())
                .build();
        acctParams.setBizUserId(id.toString());

        return memberService.signContract(acctParams);
    }

    @Override
    public String signContractQuery(Long id) {

        SignContractQueryParams params = SignContractQueryParams.builder()
                .jumpUrl(PayUtil.getNotifyUrl(memberService, NotifyEnum.SIGN_CONTRACT_QUERY_H5))
                .source(DeviceTypeEnums.PC.getType())
                .build();
        params.setBizUserId(id.toString());

        return memberService.signContractQuery(params);
    }

    @Override
    public ApplyBindBankCardResult applyBindBankCard(ApplyBindBankCardDTO dto) {
        Long cardCheck = dto.getCardCheck();
        Boolean isSafeCard = dto.getIsSafeCard();
        String unionBank = dto.getUnionBank();

        ApplyBindBankCardParams params = ApplyBindBankCardParams.builder()
                .cardNo(dto.getCardNo())
                .phone(dto.getPhone())
                .name(dto.getName())
                .cardCheck(cardCheck != null ? cardCheck : CardCheckEnums.CARD_CHECK_7.getType())
                .identityType(IdentityTypeEnums.ID_CARD.getType())
                .identityNo(dto.getIdentityNo())
                .validate(dto.getValidate())
                .cvv2(dto.getCvv2())
                .isSafeCard(isSafeCard != null ? isSafeCard : false)
                .unionBank(StringUtils.isNoneBlank(unionBank) ? unionBank : "").build();
        params.setBizUserId(dto.getBizUserId());
        return memberService.applyBindBankCard(params);
    }


    @Override
    public BindBankCardResult bindBankCard(BindBankCardDTO dto) {
        String validate = dto.getValidate();
        String cvv2 = dto.getCvv2();

        BindBankCardParams params = BindBankCardParams.builder()
                .tranceNum(dto.getTranceNum())
                .transDate(dto.getTransDate())
                .phone(dto.getPhone())
                .verificationCode(dto.getVerificationCode())
                .validate(StringUtils.isNoneBlank(validate) ? validate : "")
                .cvv2(StringUtils.isNoneBlank(cvv2) ? cvv2 : "")
                .build();
        params.setBizUserId(dto.getBizUserId());
        return memberService.bindBankCard(params);
    }

    @Override
    public SendVerificationCodeResult sendVerificationCode(Long shopId, String phone, VerificationCodeTypeEnums codeTypeEnums) {
        SendVerificationCodeParams params = SendVerificationCodeParams.builder()
                .phone(phone)
                .verificationCodeType(codeTypeEnums.getType())
                .build();
        params.setBizUserId(shopId.toString());
        return memberService.sendVerificationCode(params);
    }

    @Override
    public BindPhoneResult bindPhone(Long shopId, String phone, String verificationCode) {
        BindPhoneParams params = BindPhoneParams.builder()
                .phone(phone)
                .verificationCode(verificationCode)
                .build();
        params.setBizUserId(shopId.toString());

        return memberService.bindPhone(params);
    }

    @Override
    public MemberInfoResult getMemberInfo(Long id) {
        AllinPayBaseServiceParams params = new AllinPayBaseServiceParams(id.toString());
        return memberService.getMemberInfo(params);
    }

    @Override
    public VspTermidServiceResult bindVspTermid(Long id) {
        AllinPayConfig config = memberService.getConfig();

        VspTermidServiceParams params = VspTermidServiceParams.builder()
                .operationType(OperationTypeEnum.SET.getCode())
                .vspMerchantid(config.getVspMerchantId())
                .vspCusid(config.getVspCusId())
                .appid(config.getVspMerchantAppId())
                .vspTermid(config.getVspCusId() + "01")
                .build();
        params.setBizUserId(id.toString());

        return memberService.vspTermidService(params);
    }
}
