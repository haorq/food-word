package com.meiyuan.catering.core.constant;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/4 16:03
 */
public class UserWxXinSecretConstant {

    /**
     * 从缓存中获取 access_token 的 key
     */
    public static final String ACCESS_TOKEN_KEY = "weChatUtils_accessTokenKey";
    /**
     * 获取公众号用户 openid 的 grant_type
     */
    public static final String GRANT_TYPE_ACCESS_TOKEN = "client_credential";
    /**
     * 获公众号用户openId 请求地址
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    /**
     * 获取用户信息包含（unionId） 请求地址
     */
    public static final String PUBLIC_USER_IN_FO_URL = "https://api.weixin.qq.com/cgi-bin/user/info";

    /**
     * 临时二维码
     */
    public final static String QR_SCENE = "QR_SCENE";
    /**
     * 临时二维码（字符串）
     */
    public final static String QR_STR_SCENE = "QR_STR_SCENE";
    /**
     * 永久二维码
     */
    public static final String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";
    /**
     * 永久二维码(字符串)
     */
    public static final String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";
    /**
     * 创建二维码
     */
    public static final String CREATE_TICKET_PATH = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
    /**
     * 通过ticket换取二维码
     */
    public static final String SHOW_QR_CODE_PATH = "https://mp.weixin.qq.com/cgi-bin/showqrcode";

    /**
     * 返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     */
    public static final String LANG = "zh_CN";
    /** 公众号数据验证-勿删*/
    public static final String VERIFY_CODE = "om8hgwn9rn";
}
