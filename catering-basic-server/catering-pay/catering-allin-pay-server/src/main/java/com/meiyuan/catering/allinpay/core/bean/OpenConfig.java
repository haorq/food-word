package com.meiyuan.catering.allinpay.core.bean;

import com.meiyuan.catering.allinpay.core.util.OpenUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/9/26 14:04
 * @since v1.1.0
 */
@Data
public class OpenConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private String url;
    private String appId;
    private String secretKey;
    private String certPath;
    private String certPwd;
    private String tlCertPath;
    private String format = "JSON";
    private String charset = "utf-8";
    private String signType = "SHA256WithRSA";
    private String version = "1.0";
    private String notifyUrl;

    public OpenConfig(String url, String appId, String secretKey, String certPath, String certPwd, String tlCertPath) {
        this.url = url;
        this.appId = appId;
        this.secretKey = secretKey;
        this.certPath = certPath;
        this.certPwd = certPwd;
        this.tlCertPath = tlCertPath;
    }

    public OpenConfig() {
    }

    public void validate() {
        OpenUtils.assertNotNull(this.url, "property url must be configured");
        OpenUtils.assertNotNull(this.appId, "property appId must be configured");
        OpenUtils.assertNotNull(this.secretKey, "property secretKey must be configured");
        OpenUtils.assertNotNull(this.certPath, "property certPath must be configured");
        OpenUtils.assertNotNull(this.certPwd, "property certPwd must be configured");
        OpenUtils.assertNotNull(this.tlCertPath, "property tlCertPath must be configured");
    }
}
