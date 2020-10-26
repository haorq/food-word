package com.meiyuan.catering.allinpay.autoconfigure;

import com.meiyuan.catering.allinpay.core.OpenClient;
import com.meiyuan.catering.allinpay.utils.AllinPayOpenClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AllinPayAutoConfiguration {

    @Autowired
    private AllinPayConfig allinPayProperties;

    @Bean
    public AllinPayOpenClient allinPayOpenClient() throws Exception {
        OpenClient openClient = new OpenClient(allinPayProperties);
        return new AllinPayOpenClient(openClient);
    }


//    private OpenClient openClient() throws Exception {
////        String url = allinPayProperties.getUrl();
////        String appId = allinPayProperties.getAppId();
////        String secretKey = allinPayProperties.getSecretKey();
////        String certPath = allinPayProperties.getCertPath();
////        String tlCertPath = allinPayProperties.getTlCertPath();
////        String certPwd = allinPayProperties.getCertPwd();
////        OpenConfig oc = new OpenConfig(url, appId, secretKey, certPath, certPwd, tlCertPath);
//        return new OpenClient(allinPayProperties);
//    }


}
