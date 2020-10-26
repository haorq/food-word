package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

@Data
public class CreateMemberResult extends AllinPayBaseResponseResult {

    /**
     * userId
     */
    private String userId;
}
