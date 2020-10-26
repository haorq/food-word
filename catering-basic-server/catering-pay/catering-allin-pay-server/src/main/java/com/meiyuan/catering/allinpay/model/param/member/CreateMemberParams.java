package com.meiyuan.catering.allinpay.model.param.member;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateMemberParams extends AllinPayBaseServiceParams {

    /**
     * 类型: Integer
     * 是否必填：是
     * 描述: 用户类型
     * 示例值: 企业会员 	2 人会员 	3
     */
    private Long memberType;
    /**
     * 类型: Integer
     * 是否必填：是
     * 描述: 访问终端类型
     * 示例值: Mobile 	1  PC 	2
     */
    private Long source;
}
