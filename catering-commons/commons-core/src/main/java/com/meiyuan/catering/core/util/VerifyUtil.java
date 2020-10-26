package com.meiyuan.catering.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description：校验工具类
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/6 15:45
 */
public class VerifyUtil {

    private static final String PHONE_REGEX = "^(1[3-9][0-9])\\d{8}$";

    /**
     * describe: 校验手机号
     * @author: yy
     * @date: 2020/8/6 15:51
     * @param phone
     * @return: {@link boolean}
     * @version 1.3.0
     **/
    public static boolean verifyPhone(String phone){
        if(BaseUtil.isEmptyStr(phone)){
            return false;
        }
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }


        /**
         * 将字节数组转换为十六进制字符串
         *
         * @param byteArray
         * @return
         */
        private static String byteToStr(byte[] byteArray) {
            String strDigest = "";
            for (int i = 0; i < byteArray.length; i++) {
                strDigest += byteToHexStr(byteArray[i]);
            }
            return strDigest;
        }

        /**
         * 将字节转换为十六进制字符串
         *
         * @param mByte
         * @return
         */
        private static String byteToHexStr(byte mByte) {
            char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
            char[] tempArr = new char[2];
            tempArr[0] = digit[(mByte >>> 4) & 0X0F];
            tempArr[1] = digit[mByte & 0X0F];

            String s = new String(tempArr);
            return s;
        }
}
