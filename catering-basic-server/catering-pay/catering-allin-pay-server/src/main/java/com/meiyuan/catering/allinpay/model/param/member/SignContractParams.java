package com.meiyuan.catering.allinpay.model.param.member;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignContractParams extends AllinPayBaseServiceParams {
    /**
     * 签订成功后，跳转返回的页面地址，jumpUrl页面返回
     * {
     * "code":"", // 10000 成功
     * "msg":"",
     * "subCode":"", // OK 成功
     * "subMsg":"",
     * "sign":"",
     * "data":{
     * "bizUserId":"", //商户系统用户标识，商户系统中唯一编号。
     * "contractNo":"", //会员电子协议编号
     * "result":"" // 签订结果 成功：OK ，失败：error
     * }
     * }
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述:  	签订之后，跳转返回的页面地址
     * 示例值:
     */
    private String jumpUrl;

    /**
     * 类型: String
     * 是否必填：是
     * 描述: 后台通知地址
     * 示例值:
     */
    private String backUrl;
    /**
     * 类型: String
     * 是否必填：否
     * 描述: 会员取消签约，跳转返回的页面地址
     * 示例值:
     */
    private String noContractUrl;

    /**
     * 类型: Long
     * 是否必填：是
     * 描述: 访问终端类型
     * 示例值:
     */
    private Long source;
}
