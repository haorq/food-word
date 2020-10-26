package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/24 18:26
 * @since v1.1.0
 */
@Data
public class SetRealNameResult extends AllinPayBaseResponseResult {

    /**
     * 姓名
     */
    public String name;
    /**
     * 证件类型
     */
    public Long identityType;
    /**
     * 证件号码
     */
    public String identityNo;
}
