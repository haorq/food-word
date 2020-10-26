package com.meiyuan.catering.allinpay.model.bean.notify;

import lombok.Data;

/**
 * 描述:会员电子协议签约通知结果
 *
 * @author zengzhangni
 * @date 2020/9/30 14:07
 * @since v1.5.0
 */
@Data
public class SignContractNotifyResult {

    /**
     * 商户系统用户标识，商户系统中唯一编号。
     */
    private String bizUserId;
    /**
     * 会员电子协议编号
     */
    private String contractNo;
    /**
     * 签订结果 	成功：OK ，失败：error
     */
    private String status;

}
