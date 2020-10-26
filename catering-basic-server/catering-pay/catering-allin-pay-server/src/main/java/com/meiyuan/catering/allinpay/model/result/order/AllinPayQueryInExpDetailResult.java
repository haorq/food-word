package com.meiyuan.catering.allinpay.model.result.order;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/27 18:09
 * @description 通联查询账户收支明细接口返回值
 **/

@Data
public class AllinPayQueryInExpDetailResult {

    /**
     * 商户系统用户标识，商户系统中唯一编号。
     *
     * 个人会员、企业会员返回bizUserId
     * 平台返回：#yunBizUserId_B2C#
     */
    private String bizUserId;
    /**
     * 该账户收支明细总条数
     */
    private Long totalNum;
    /**
     * 收支明细
     */
    private JSONArray inExpDetail;
    /**
     * 扩展参数
     *
     * 订单申请上送的扩展参数信息，不可包含“|”特殊字符
     */
    private String extendInfo;

}
