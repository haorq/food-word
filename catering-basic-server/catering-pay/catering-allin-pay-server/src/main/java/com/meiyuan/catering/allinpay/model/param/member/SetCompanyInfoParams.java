package com.meiyuan.catering.allinpay.model.param.member;

import com.meiyuan.catering.allinpay.model.bean.member.CompanyBasicInfo;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/24 19:52
 * @since v1.1.0
 */
@Data
public class SetCompanyInfoParams extends AllinPayBaseServiceParams {

    /**
     * 类型: String
     * 是否必填：否
     * 描述: 企业会员审核结果通知
     * 示例值: 企业会员审核成功或者失败，将会发送后台通知
     */
    private String backUrl;
    /**
     * 类型: JSONObject
     * 是否必填：是
     * 描述: 企业基本信息
     * 示例值:
     */
    private CompanyBasicInfo companyBasicInfo;

    /**
     * 类型: String
     * 是否必填：是
     * 描述: 是否进行线上认证
     * 示例值: true：系统自动审核 false：需人工审核
     */
    private Boolean isAuth;

//    /**
//     * 企业扩展信息 目前不需要传
//     */
//    private JSONObject companyExtendInfo;
}
