package com.meiyuan.catering.allinpay.model.param.member;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/10/17 11:33
 * @since v1.5.0
 */
@Data
@Builder
public class VspTermidServiceParams extends AllinPayBaseServiceParams {

    /**
     * set-绑定 query-查询
     */
    private String operationType;

    /**
     * 集团模式：集团商户收银宝商户号 单商户模式：不填
     */
    private String vspMerchantid;
    /**
     * 单商户模式：商户收银宝商户号 集团模式：收银宝子商户号
     */
    private String vspCusid;
    /**
     * 单商户模式：上送单商户号 appid 集团模式：上送集团商户号 appid 平台在收银宝端唯一，多个收银宝 商户时需配置共享参数。
     */
    private String appid;
    /**
     * 1、绑定时必填 (1)收银宝 POS 及当面付：按实际终 端号填写；
     * (2)收银宝集团模式-公众号/小程序 /APP/正扫/反扫，填写“收银宝子 商户号+2 位序号（01/02 以此类 推）”，
     * 如：55058404816VQLX01、 55058404816VQLX02 2、查询时不填则返回终端列表信 息，填写返回指定终端信息
     */
    private String vspTermid;

}
