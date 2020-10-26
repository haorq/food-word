package com.meiyuan.catering.allinpay.model.result.member;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.allinpay.enums.service.member.MemberTypeEnums;
import com.meiyuan.catering.allinpay.model.bean.member.CompanyInfo;
import com.meiyuan.catering.allinpay.model.bean.member.MemberBaseInfo;
import com.meiyuan.catering.allinpay.model.bean.member.MemberInfo;
import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 9:19
 * @since v1.1.0
 */
@Data
public class MemberInfoResult extends AllinPayBaseResponseResult {

    private Long memberType;

    private JSONObject memberInfo;

    public MemberBaseInfo memberInfoByType() {
        String infoStr = JSONObject.toJSONString(memberInfo);
        if (memberType.intValue() == MemberTypeEnums.ENTERPRISE_MEMBER.getType()) {
            return JSONObject.parseObject(infoStr, CompanyInfo.class);
        } else {
            return JSONObject.parseObject(infoStr, MemberInfo.class);
        }
    }
}
