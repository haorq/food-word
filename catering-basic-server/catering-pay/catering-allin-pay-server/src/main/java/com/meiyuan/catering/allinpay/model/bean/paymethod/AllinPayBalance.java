package com.meiyuan.catering.allinpay.model.bean.paymethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

/**
 * created on 2020/8/18 9:39
 * 账户余额 账户内转 账
 * @author yaozou
 * @since v1.0.0
 */
@Data
@Builder
public class AllinPayBalance {
    /** 账户集编号 */
    private String  accountSetNo;
    private Integer amount;

}
