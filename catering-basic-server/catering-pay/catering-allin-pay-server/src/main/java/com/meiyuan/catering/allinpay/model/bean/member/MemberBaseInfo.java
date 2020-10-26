package com.meiyuan.catering.allinpay.model.bean.member;

import lombok.Data;

@Data
public class MemberBaseInfo {

    /**
     * 通商云用户 id
     */
    private String userId;

    /**
     * 会员子账户开户的子账户号
     */
    private String subAcctNo;
    /**
     * 是否已签电子协议
     */
    private Boolean isSignContract;
    /**
     * 合同号
     */
    private String contractNo;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 备注
     * 示例值:
     */
    private String remark;

}
