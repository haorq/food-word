package com.meiyuan.catering.allinpay.service.impl.allinpay;

import com.meiyuan.catering.allinpay.enums.service.ApiEnums;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import com.meiyuan.catering.allinpay.model.param.member.*;
import com.meiyuan.catering.allinpay.model.result.member.*;
import com.meiyuan.catering.allinpay.service.IMemberService;
import com.meiyuan.catering.allinpay.utils.AllinPayOpenClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllinPayMemberServiceImpl extends AllinPayServiceImpl implements IMemberService {

    @Autowired
    private AllinPayOpenClient client;

    @Override
    public CreateMemberResult createMember(CreateMemberParams params) {
        return client.execute(ApiEnums.CREATE_MEMBER, params, CreateMemberResult.class);
    }

    @Override
    public SendVerificationCodeResult sendVerificationCode(SendVerificationCodeParams params) {
        return client.execute(ApiEnums.SEND_VERIFICATION_CODE, params, SendVerificationCodeResult.class);
    }

    @Override
    public BindPhoneResult bindPhone(BindPhoneParams params) {
        return client.execute(ApiEnums.BIND_PHONE, params, BindPhoneResult.class);
    }

    @Override
    public String signContract(SignContractParams params) {
        return client.concatUrlParams(ApiEnums.SIGN_CONTRACT, params);
    }

    @Override
    public SetRealNameResult setRealName(SetRealNameParams params) {
        return client.execute(ApiEnums.SET_REAL_NAME, params, SetRealNameResult.class);
    }

    @Override
    public SetCompanyResult setCompanyInfo(SetCompanyInfoParams params) {
        return client.execute(ApiEnums.SET_COMPANY_INFO, params, SetCompanyResult.class);
    }

    @Override
    public MemberInfoResult getMemberInfo(AllinPayBaseServiceParams params) {
        return client.execute(ApiEnums.GET_MEMBER_INFO, params, MemberInfoResult.class);
    }

    @Override
    public GetBankCardBinResult getBankCardBin(GetBankCardBinParams params) {
        return client.execute(ApiEnums.GET_BANK_CARD_BIN, params, GetBankCardBinResult.class);
    }

    @Override
    public ApplyBindBankCardResult applyBindBankCard(ApplyBindBankCardParams params) {
        return client.execute(ApiEnums.APPLY_BIND_BANK_CARD, params, ApplyBindBankCardResult.class);
    }

    @Override
    public BindBankCardResult bindBankCard(BindBankCardParams params) {
        return client.execute(ApiEnums.BIND_BANK_CARD, params, BindBankCardResult.class);
    }

    @Override
    public QueryBankCardResult queryBankCard(QueryBankCardParams params) {
        return client.execute(ApiEnums.QUERY_BANK_CARD, params, QueryBankCardResult.class);
    }

    @Override
    public UnbindBankCardResult unbindBankCard(UnbindBankCardParams params) {
        return client.execute(ApiEnums.UNBIND_BANK_CARD, params, UnbindBankCardResult.class);
    }

    @Override
    public LockMemberResult lockMember(LockMemberParams params) {
        return client.execute(ApiEnums.LOCK_MEMBER, params, LockMemberResult.class);
    }

    @Override
    public UnlockMemberResult unlockMember(UnlockMemberParams params) {
        return client.execute(ApiEnums.UNLOCK_MEMBER, params, UnlockMemberResult.class);
    }

    @Override
    public void setPayPwd() {

    }

    @Override
    public void updatePayPwd() {

    }

    @Override
    public void resetPayPwd() {

    }

    @Override
    public void updatePhoneByPayPwd() {

    }

    @Override
    public ApplyBindAcctResult applyBindAcct(ApplyBindAcctParams params) {
        return client.execute(ApiEnums.APPLY_BIND_ACCT, params, ApplyBindAcctResult.class);
    }

    @Override
    public UnbindPhoneResult unbindPhone(UnbindPhoneParams params) {
        return client.execute(ApiEnums.UNBIND_PHONE, params, UnbindPhoneResult.class);
    }

    @Override
    public String signContractQuery(SignContractQueryParams params) {
        return client.concatUrlParams(ApiEnums.SIGN_CONTRACT_QUERY, params);
    }

    @Override
    public VspTermidServiceResult vspTermidService(VspTermidServiceParams params) {
        return client.execute(ApiEnums.VSP_TERMID_SERVICE, params, VspTermidServiceResult.class);
    }
}
