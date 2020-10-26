package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.bean.member.VspTermid;
import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/10/17 11:36
 * @since v1.5.0
 */
@Data
public class VspTermidServiceResult extends AllinPayBaseResponseResult {

    /**
     * 绑定、查询收银宝终端号结果 成功：OK 失败：error
     */
    private String result;
    /**
     * 已绑定收银宝终端号列表 详细如没有查到终端号，则返回空数组
     */
    private List<VspTermid> vspTermidList;
}
