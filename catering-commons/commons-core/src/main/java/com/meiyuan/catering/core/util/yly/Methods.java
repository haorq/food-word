package com.meiyuan.catering.core.util.yly;


import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author LiuHen
 * @Date 2020/9/26 0026 15:12
 * @Description 简单描述 : 易联云接口工具类
 * @Since version-1.4.0
 */
@Slf4j
public class Methods {
    /**
     * 易联云颁发给开发者的应用ID 非空值
     */
    public static String CLIENT_ID;

    /**
     * 易联云颁发给开发者的应用secret 非空值
     */
    public static String CLIENT_SECRET;

    /**
     * token
     */
    public static String token;

    /**
     * 刷新token需要的 refreshtoken
     */
    public static String refresh_token;

    /**
     * code
     */
    public static String CODE;

    private Methods() {
    }

    private static class SingleMethods {
        private static final Methods COCOS_MANGER = new Methods();
    }

    public static final Methods getInstance() {
        return SingleMethods.COCOS_MANGER;
    }

    /**
     * 开放式初始化
     *
     * @param client_id
     * @param client_secret
     * @param code
     */
    public void init(String client_id, String client_secret, String code) {
        CLIENT_ID = client_id;
        CLIENT_SECRET = client_secret;
        CODE = code;
    }

    /**
     * 自有初始化
     *
     * @param client_id
     * @param client_secret
     */
    public void init(String client_id, String client_secret) {
        CLIENT_ID = client_id;
        CLIENT_SECRET = client_secret;
    }

    /**
     * 开放应用
     */
    public String getToken() {
        String result = LAVApi.getToken(CLIENT_ID,
                "authorization_code",
                LAVApi.getSin(),
                CODE,
                "all",
                String.valueOf(System.currentTimeMillis() / 1000),
                LAVApi.getuuid());
        try {
            JSONObject json = new JSONObject(result);
            JSONObject body = json.getJSONObject("body");
            token = body.getString("access_token");
            refresh_token = body.getString("refresh_token");
        } catch (JSONException e) {
            log.error("getToken出现Json异常！",e);
        }
        return result;
    }

    /**
     * 自有应用服务
     */
    public String getFreedomToken() {
        String result = LAVApi.getToken(CLIENT_ID,
                "client_credentials",
                LAVApi.getSin(),
                "all",
                String.valueOf(System.currentTimeMillis() / 1000),
                LAVApi.getuuid());
        try {
            JSONObject json = new JSONObject(result);
            JSONObject body = json.getJSONObject("body");
            token = body.getString("access_token");
            refresh_token = body.getString("refresh_token");
        } catch (JSONException e) {
            log.error("getFreedomToken出现Json异常！",e);
        }
        return result;
    }

    /**
     * 刷新token
     */
    public String refreshToken() {
        String result = LAVApi.refreshToken(CLIENT_ID,
                "refresh_token",
                "all",
                LAVApi.getSin(),
                refresh_token,
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
        try {
            JSONObject json = new JSONObject(result);
            JSONObject body = json.getJSONObject("body");
            token = body.getString("access_token");
            refresh_token = body.getString("refresh_token");
        } catch (JSONException e) {
            log.error("refreshToken出现Json异常！",e);
        }
        return result;
    }

    /**
     * 方法描述 : 添加终端授权 开放应用服务模式不需要此接口 ,自有应用服务模式所需参数【永久授权】
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:07
     * @param machine_code 易联云打印机终端号
     * @param msign  易联云终端密钥
     * @param print_name 打印机名（选填）
     * @return: java.lang.String
     * @Since version-1.4.0
     */
    public String addPrinter(String machine_code, String msign,String print_name) {
        return LAVApi.addPrinter(CLIENT_ID,
                machine_code,
                msign,
                token,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000),
                print_name);
    }

    /**
     * 极速授权
     */
    public String speedAu(String machine_code, String qr_key) {
        return LAVApi.speedAu(CLIENT_ID,
                machine_code,
                qr_key,
                "all",
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 打印
     */
    public String print(String machine_code, String content, String origin_id,String printName) {
        return LAVApi.print(CLIENT_ID,
                token,
                machine_code,
                content,
                origin_id,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000),
                printName);
    }

    /**
     * 方法描述 : 删除终端授权
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:09
     * @param machine_code 	易联云打印机终端号
     * @return: java.lang.String
     * @Since version-1.4.0
     */
    public String deletePrinter(String machine_code) {
        return LAVApi.deletePrinter(CLIENT_ID,
                token,
                machine_code,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 添加应用菜单
     */
    public String addPrintMenu(String machine_code, String content) {
        return LAVApi.addPrintMenu(CLIENT_ID,
                token,
                machine_code,
                content,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 关机重启接口
     */
    public String shutDownRestart(String machine_code, String response_type) {
        return LAVApi.shutDownRestart(CLIENT_ID,
                token,
                machine_code,
                response_type,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 声音调节
     */
    /**
     * 方法描述 :声音调节 - [声音类型、音量]
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:14
     * @param machine_code
     * @param response_type 蜂鸣器:buzzer,喇叭:horn
     * @param voice [0,1,2,3] 音量类型
     * @return: java.lang.String
     * @Since version-1.4.0
     */
    public String setSound(String machine_code, String response_type, String voice) {
        return LAVApi.setSound(CLIENT_ID,
                token,
                machine_code,
                response_type,
                voice,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 获取机型打印宽度接口
     */
    public String getPrintInfo(String machine_code) {
        return LAVApi.getPrintInfo(CLIENT_ID,
                token,
                machine_code,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 获取机型软硬件版本接口
     */
    public String getVersion(String machine_code) {
        return LAVApi.getVersion(CLIENT_ID,
                token,
                machine_code,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 取消所有未打印订单
     */
    public String cancelAll(String machine_code) {
        return LAVApi.cancelAll(CLIENT_ID,
                token,
                machine_code,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 取消单条未打印订单
     */
    public String cancelOne(String machine_code, String order_id) {
        return LAVApi.cancelOne(CLIENT_ID,
                token,
                machine_code,
                order_id,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 方法描述 : 设置打印logo
     *             1、设置成功以后每次打印订单将会在订单头部打印logo
     *             2、格式要求：图片宽度最大为350px,文件大小不能超过40Kb
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 14:57
     * @param machine_code 易联云打印机终端号
     * @param img_url logourl
     * @return: String 设置结果
     * @Since version-1.4.0
     */
    public String setIcon(String machine_code, String img_url) {
        return LAVApi.setIcon(CLIENT_ID,
                token,
                machine_code,
                img_url,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 删除logo
     */
    public String deleteIcon(String machine_code) {
        return LAVApi.deleteIcon(CLIENT_ID,
                token,
                machine_code,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 打印方式
     */
    public String btnPrint(String machine_code, String response_type) {
        return LAVApi.btnPrint(CLIENT_ID,
                token,
                machine_code,
                response_type,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 接单拒单设置接口
     */
    public String getOrder(String machine_code, String response_type) {
        return LAVApi.getOrder(CLIENT_ID,
                token,
                machine_code,
                response_type,
                LAVApi.getSin(),
                LAVApi.getuuid(),
                String.valueOf(System.currentTimeMillis() / 1000));
    }

}
