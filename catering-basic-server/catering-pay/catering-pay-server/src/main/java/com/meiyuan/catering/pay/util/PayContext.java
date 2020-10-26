package com.meiyuan.catering.pay.util;

import com.github.binarywang.wxpay.service.WxPayService;
import com.meiyuan.catering.allinpay.service.IMemberService;
import com.meiyuan.catering.allinpay.service.IOrderService;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.util.SpringContextUtils;
import com.meiyuan.catering.merchant.feign.ShopBankClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.pay.enums.PayEnum;
import com.meiyuan.catering.pay.service.MyPayService;

/**
 * @author zengzhangni
 * @date 2020/9/29 14:46
 * @since v1.5.0
 */
public class PayContext {

    public static MyPayService getPayService(Integer payWay) {
        PayEnum payEnum = PayEnum.parse(payWay);
        return getPayService(payEnum);
    }

    public static MyPayService getPayService(PayWayEnum payWay) {
        PayEnum payEnum = PayEnum.parse(payWay.getPayWay());
        return getPayService(payEnum);
    }

    public static MyPayService getPayService(PayEnum payEnum) {
        return SpringContextUtils.getBean(payEnum.getImpl(), MyPayService.class);
    }

    public static IOrderService getIOrderService() {
        return SpringContextUtils.getBean(IOrderService.class);
    }

    public static IMemberService getIMemberService() {
        return SpringContextUtils.getBean(IMemberService.class);
    }

    public static WxPayService getWxPayService() {
        return SpringContextUtils.getBean(WxPayService.class);
    }

    public static MerchantUtils getMerchantUtils() {
        return SpringContextUtils.getBean(MerchantUtils.class);
    }

    public static ShopBankClient getShopBankClient() {
        return SpringContextUtils.getBean(ShopBankClient.class);
    }

}
