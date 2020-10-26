package com.meiyuan.catering.allinpay.core.util;


import com.meiyuan.catering.allinpay.core.bean.CustReq;

import java.security.PrivateKey;

/**
 * @author zengzhangni
 * @date 2020/9/26 14:07
 * @since v1.1.0
 */
public class OpenUtils {
    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        } else if (object instanceof String && isEmpty((String) object)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) || "".equals(str.trim());
    }

    public static String htBankWithdrawSign(CustReq custReq, String certPath, String certPwd) throws Exception {
        assertNotNull(custReq, "custReq is null --参数custReq为空");
        assertNotNull(certPath, "certPath is null --参数certPath为空");
        assertNotNull(certPwd, "certPwd is null --参数certPwd为空");
        assertNotNull(custReq.getAmount(), "AMOUNT is null or empty --参数AMOUNT为空");
        assertNotNull(custReq.getPayeeAcctName(), "PAYEE_ACCT_NAME is null or empty --参数PAYEE_ACCT_NAME为空");
        assertNotNull(custReq.getPayeeAcctNo(), "PAYEE_ACCT_NO is null or empty --参数PAYEE_ACCT_NO为空");
        PrivateKey pk = SecretUtils.loadPrivateKey(null, certPath, certPwd);
        return SecretUtils.htSign(pk, custReq.toString());
    }

    private OpenUtils() {
    }
}
