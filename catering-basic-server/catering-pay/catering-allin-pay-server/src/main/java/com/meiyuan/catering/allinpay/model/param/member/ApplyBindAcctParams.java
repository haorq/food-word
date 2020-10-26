package com.meiyuan.catering.allinpay.model.param.member;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 11:52
 * @since v1.1.0
 */
@Data
@Builder
public class ApplyBindAcctParams extends AllinPayBaseServiceParams {

    /**
     * 类型: String
     * 是否必填：是
     * 描述: 操作
     * 示例值: set-绑定
     */
    private String operationType;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 支付账户类型
     * 示例值:  weChatPublic -微信公众号 weChatMiniProgram -微信小程序 aliPayService -支付宝生活号
     */
    private String acctType;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 支付账户用户标识
     * 示例值：微信公众号支付openid——微信分配 微信小程序支付openid——微信分配
     * 支付宝生活号支付user_id——支付宝分配
     * openid示例oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     */
    private String acct;
}
