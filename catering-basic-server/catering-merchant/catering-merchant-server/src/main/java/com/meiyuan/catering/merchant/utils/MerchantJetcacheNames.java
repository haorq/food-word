package com.meiyuan.catering.merchant.utils;

/**
 * @Author MeiTao
 * @Description 商户相关缓存key
 * @Date  2020/3/12 0012 10:27
 */
public class MerchantJetcacheNames {
    /** 验证码 */
    public final static String SHOP_CONFIG = "shop:config";

    /** 商户编码 */
    public static final String MERCHANT_CODE_KEY = "merchant:code:";

    /** 商户app端token */
    public final static String MERCHANT_TOKEN = "merchant:token:";

    /** 商户后台 token */
    public final static String MERCHANT_PC_TOKEN = "merchant:pc:token:";

    /** 忘记密码 */
    public final static String MERCHANT_PSW_CODE = "merchant:psw:code:";

    /** 商户pc端 忘记密码 短信验证码*/
    public final static String MERCHANT_PC_PSW_CODE = "merchantPc:psw:code:";

}
