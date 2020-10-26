package com.meiyuan.catering.allinpay.core;

/**
 * @author zengzhangni
 * @date 2020/9/26 14:08
 * @since v1.1.0
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.meiyuan.catering.allinpay.autoconfigure.AllinPayConfig;
import com.meiyuan.catering.allinpay.core.bean.BizParameter;
import com.meiyuan.catering.allinpay.core.bean.OpenRequest;
import com.meiyuan.catering.allinpay.core.bean.OpenResponse;
import com.meiyuan.catering.allinpay.core.bean.OpenServiceRequest;
import com.meiyuan.catering.allinpay.core.util.OpenUtils;
import com.meiyuan.catering.allinpay.core.util.SecretUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Data
public class OpenClient {

    private static final String CHARSET = "utf-8";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();
    private final AllinPayConfig config;
    private final RSAPrivateKey privateKey;
    private final PublicKey tlPublicKey;


    public OpenClient(AllinPayConfig config) throws Exception {
        this.config = config;
        this.privateKey = (RSAPrivateKey) SecretUtils.loadPrivateKey(null, config.getCertPath(), config.getCertPwd());
        this.tlPublicKey = SecretUtils.loadTLPublicKey(config.getTlCertPath());
    }

    public OpenResponse execute(String method, BizParameter param) throws Exception {
        OpenRequest request = this.assembleRequest(method, param);
        log.info("request:" + request);
        String resp = this.post(request);
        log.info("response:{}", resp);
        return this.verify(resp);
    }

    public String concatUrlParams(String method, BizParameter param) throws Exception {
        OpenRequest request = this.assembleRequest(method, param);
        return this.config.getUrl() + "?" + this.encodeOnce(request);
    }

    public String concatUrlForServer(String method, BizParameter param, String jumpUrl, String clientAppId) throws Exception {
        OpenRequest request = this.assembleServiceRequest(method, param, jumpUrl, clientAppId);
        return this.config.getUrl() + "?" + this.encodeOnce(request);
    }

    public String encrypt(String content) {
        return SecretUtils.encryptAES(content, this.config.getSecretKey());
    }

    public String decrypt(String content) {
        return SecretUtils.decryptAES(content, this.config.getSecretKey());
    }

    public boolean checkSign(String signedValue, String sign) throws Exception {
        return SecretUtils.verify(this.tlPublicKey, signedValue, sign);
    }

    private OpenResponse verify(String resp) throws Exception {
        JSONObject map = JSON.parseObject(resp);
        String sign = map.getString("sign");
        map.remove("sign");
        String signedValue = JSON.toJSONString(map, SerializerFeature.MapSortField);
        System.out.println(signedValue);
        if (!SecretUtils.verify(this.tlPublicKey, signedValue, sign)) {
            throw new Exception("verify sign error");
        } else {
            return JSON.parseObject(resp, OpenResponse.class);
        }
    }

    public Boolean verifyResult(String result) throws Exception {
        TreeMap<String, String> map = new TreeMap<>();

        String[] notifyParamsArr = result.split("&");
        for (String notifyParam : notifyParamsArr) {
            String[] split = notifyParam.split("=");
            map.put(split[0], split[1]);
        }
        String sign = map.get("sign");
        map.remove("sign");
        map.remove("signType");

        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if (v != null) {
                sb.append("&").append(k).append("=").append(v);
            }
        });
        String signedValue = URLDecoder.decode(sb.substring(1), "UTF-8");

        String decode = URLDecoder.decode(sign, "UTF-8");

        return checkSign(signedValue, decode);

    }

    private OpenRequest assembleServiceRequest(String method, BizParameter param, String jumpUrl, String clientAppId) throws Exception {
        OpenUtils.assertNotNull(param, "param must not be null");
        OpenServiceRequest request = new OpenServiceRequest();
        request.setAppId(this.config.getAppId());
        request.setClientAppId(clientAppId);
        request.setJumpUrl(jumpUrl);
        request.setMethod(method);
        request.setFormat(this.config.getFormat());
        request.setCharset(this.config.getCharset());
        request.setTimestamp(DATE_FORMAT.format(new Date()));
        request.setVersion(this.config.getVersion());
        request.setNotifyUrl(this.config.getNotifyUrl());
        request.setBizContent(param.toString());
        String signedValue = this.getSignedValue(request);
        log.debug("待签名源串：" + signedValue);
        String sign = SecretUtils.sign(this.privateKey, signedValue, this.config.getSignType());
        request.setSignType(this.config.getSignType());
        request.setSign(sign);
        return request;
    }

    private OpenRequest assembleRequest(String method, BizParameter param) throws Exception {
        OpenUtils.assertNotNull(param, "param must not be null");
        OpenRequest request = new OpenRequest();
        request.setAppId(this.config.getAppId());
        request.setMethod(method);
        request.setFormat(this.config.getFormat());
        request.setCharset(this.config.getCharset());
        request.setTimestamp(DATE_FORMAT.format(new Date()));
        request.setVersion(this.config.getVersion());
        request.setNotifyUrl(this.config.getNotifyUrl());
        request.setBizContent(param.toString());
        String signedValue = this.getSignedValue(request);
        log.debug("待签名源串：" + signedValue);
        String sign = SecretUtils.sign(this.privateKey, signedValue, this.config.getSignType());
        request.setSignType(this.config.getSignType());
        request.setSign(sign);
        return request;
    }

    private String getSignedValue(OpenRequest request) {
        Map<String, Object> reqMap = (JSONObject) JSON.toJSON(request);
        Map<String, String> copy = new TreeMap<>();

        reqMap.forEach((k, v) -> {
            if (v != null) {
                copy.put(k, v.toString());
            }
        });

        StringBuilder sb = new StringBuilder();

        copy.forEach((k, v) -> {
            if (v != null) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1);
    }


    private String post(OpenRequest requestParams) throws IOException {
        String requestBody = JSON.toJSONString(requestParams);
        FormBody.Builder builder = new FormBody.Builder();
        JSONObject json = JSONObject.parseObject(requestBody);
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            builder.add(entry.getKey(), entry.getValue().toString());
        }
        FormBody formBody = builder.build();
        log.info("allinpay request params:{}", requestBody);
        Request request = new Request.Builder()
                .url(this.config.getUrl())
                .post(formBody)
                .build();
        Call call = OK_HTTP_CLIENT.newCall(request);
        Response response = call.execute();
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        String result = body.string();
        log.info("allinpay response result:{}", result);
        return result;
    }

//    private String post(OpenRequest request) {
//        String param = this.encodeOnce(request);
//        log.info("post body:" + param);
//        StringBuilder result = new StringBuilder();
//        BufferedWriter writer = null;
//        BufferedReader reader = null;
//
//        try {
//            URL httpUrl = new URL(this.config.getUrl());
//            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
//            if (connection instanceof HttpsURLConnection) {
//                ((HttpsURLConnection) connection).setSSLSocketFactory(OpTrustManager.instance().getSSLSocketFactory());
//            }
//
//            connection.setRequestProperty("Connection", "keep-alive");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), CHARSET));
//            writer.write(param);
//            writer.flush();
//            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                result.append(line);
//            }
//        } catch (Exception var17) {
//            log.error(var17.getMessage(), var17);
//        } finally {
//            try {
//                if (writer != null) {
//                    writer.close();
//                }
//
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (IOException var16) {
//                log.error(var16.getMessage(), var16);
//            }
//
//        }
//
//        return result.toString();
//    }

    private String encodeOnce(OpenRequest request) {
        JSONObject jo = (JSONObject) JSON.toJSON(request);
        StringBuilder sb = new StringBuilder();


        jo.forEach((k, v) -> {
            String value = v == null ? "" : v.toString();
            try {
                sb.append(k).append("=")
                        .append(URLEncoder.encode(value, CHARSET)).append("&");
            } catch (UnsupportedEncodingException ignored) {
            }
        });


        return sb.toString().substring(0, sb.length() - 1);
    }

}

