package com.meiyuan.catering.allinpay.model.param.member;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import com.meiyuan.catering.allinpay.utils.DecryptDeserializer;
import com.meiyuan.catering.allinpay.utils.EncryptSerializer;
import lombok.Builder;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/24 18:25
 * @since v1.1.0
 */
@Data
@Builder
public class SetRealNameParams extends AllinPayBaseServiceParams {

    /**
     * 类型: Boolean
     * 是否必填：是
     * 描述: 是否由通商云进行认证
     * 示例值:
     */
    public Boolean isAuth;
    /**
     * 类型: String
     * 是否必填：是
     * 描述: 姓名
     * 示例值:
     */
    public String name;
    /**
     * 类型: Long
     * 是否必填：是
     * 描述: 证件类型
     * 示例值: 目前只支持身份证。 身份证 	1
     */
    public Long identityType;
    /**
     * 类型: String
     * 是否必填：是
     * 是否加密：AES加密
     * 描述: 证件号码
     * 示例值:
     */
    @JSONField(serializeUsing = EncryptSerializer.class, deserializeUsing = DecryptDeserializer.class)
    public String identityNo;
}
