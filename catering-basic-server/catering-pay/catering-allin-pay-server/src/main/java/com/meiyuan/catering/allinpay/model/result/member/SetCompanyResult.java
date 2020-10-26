package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/24 19:52
 * @since v1.1.0
 */
@Data
public class SetCompanyResult extends AllinPayBaseResponseResult {
    /**
     * 审核结果 2：审核成功。 3：审核失败。 仅自动审核时返回。
     */
    private Integer result;
    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 备注
     */
    private String remark;
}
