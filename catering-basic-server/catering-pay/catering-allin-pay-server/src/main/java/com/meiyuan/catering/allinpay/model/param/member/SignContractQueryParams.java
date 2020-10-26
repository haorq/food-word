package com.meiyuan.catering.allinpay.model.param.member;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/24 17:56
 * @since v1.1.0
 */
@Data
@Builder
public class SignContractQueryParams extends AllinPayBaseServiceParams {

    /**
     * 类型: String
     * 是否必填：是
     * 描述: 签订之后，跳转返回的页面地址
     * 示例值:
     */
    private String jumpUrl;
    /**
     * 类型: Long
     * 是否必填：是
     * 描述: 访问终端类型
     * 示例值:
     */
    private Long source;

}
