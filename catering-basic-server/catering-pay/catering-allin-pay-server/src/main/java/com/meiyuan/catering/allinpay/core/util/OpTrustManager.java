package com.meiyuan.catering.allinpay.core.util;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author zengzhangni
 * @date 2020/9/26 14:07
 * @since v1.1.0
 */
public class OpTrustManager implements X509TrustManager {
    private static volatile OpTrustManager instance;
    private SSLSocketFactory sslFactory;

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return this.sslFactory;
    }

    private OpTrustManager() {
    }

    public static OpTrustManager instance() throws NoSuchAlgorithmException, KeyManagementException {
        if (instance == null) {
            synchronized (OpTrustManager.class) {
                if (instance == null) {
                    instance = new OpTrustManager();
                    SSLContext sc = SSLContext.getInstance("SSLv3");
                    sc.init(null, new TrustManager[]{instance}, null);
                    instance.sslFactory = sc.getSocketFactory();
                }
            }
        }

        return instance;
    }
}
