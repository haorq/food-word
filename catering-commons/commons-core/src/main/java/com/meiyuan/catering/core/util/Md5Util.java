package com.meiyuan.catering.core.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @Author MeiTao
 * @Description 加密
 * @Date 2020/3/22 0022 9:40
 */
public class Md5Util {

    private final static String PAY_SALT = "balance_account";
    private final static int INT_32 = 32;

    /**
     * 使用md5的算法进行加密
     */
    public static String md5(String plainText) {

        byte[] secretBytes;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        // 16进制数字
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < INT_32 - md5code.length(); i++) {
            md5code.insert(0, "0");
        }
        return md5code.toString();
    }

    /**
     * Md5密码加密
     *
     * @param password
     * @return
     */
    public static String passwordEncrypt(String password) {
        return md5(password);
    }

    /**
     * 描述: 设置支付密码
     *
     * @param password 支付密码
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/27 10:35
     */
    public static String payPassword(String password) {
        return md5(md5(password + PAY_SALT));
    }

    /**
     * 描述: 验证支付密码
     *
     * @param payPassword   支付密码
     * @param dbPayPassword 数据库支付密码
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/3/27 10:36
     */
    public static Boolean verifyPayPassword(String payPassword, String dbPayPassword) {
        String password = payPassword(payPassword);
        return Objects.equals(password, dbPayPassword);
    }
}
