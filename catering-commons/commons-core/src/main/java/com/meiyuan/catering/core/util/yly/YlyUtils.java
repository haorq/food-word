package com.meiyuan.catering.core.util.yly;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yly.java.yly_sdk.RequestMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author MeiTao
 * @Date 2020/9/23 0023 19:22
 * @Description 简单描述 : 易联云
 * @Since version-1.4.0
 */
@Slf4j
@Service
public class YlyUtils {
    @Resource
    YlyService ylyService;

    /**
     * 方法描述 : 获取授权token
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:24
     * @return: java.lang.String
     * @Since version-1.4.0
     */
    public String getAccessToken() {
        String accessToken  = null;
        try {
            accessToken = RequestMethod.getInstance().getAccessToken();
            System.out.println("=======accessToken======= : " + accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    /**
     * 方法描述 : 终端授权 (永久授权)
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:24
     * @param machineCode
     * @param msign
     * @return: String 返回结果为空表示授权成功
     * @Since version-1.4.0
     */
    public String addPrinter(String machineCode, String msign) {
        try {
            String result = RequestMethod.getInstance().addPrinter(machineCode, msign, ylyService.getAppToken());
            Boolean b = handelResultJson(result);
            if (b){
                return  null;
            }
            return JSON.parseObject(result).getString("error_description");
        } catch (Exception e) {
            log.error("易联云打印机终端授权失败，终端号 ： " + machineCode + ",密钥 ： " + msign,e);
        }
        return null;
    }

    /**
     * 方法描述 : 打印设备设置打印logo
     *       1、设置成功以后每次打印订单将会在订单头部打印logo
     *       2、格式要求：图片宽度最大为350px,文件大小不能超过40Kb
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:25
     * @param machineCode 易联云打印机终端号
     * @param imgUrl  图片地址
     * @return: java.lang.String
     * @Since version-1.4.0
     */
    public String printSetIcon(String machineCode, String imgUrl) {
        try {
            String result = RequestMethod.getInstance().printSetIcon(ylyService.getAppToken(), machineCode, imgUrl);
            Boolean aBoolean = this.handelResultJson(result);
            if (aBoolean){
                return  null;
            }
            return JSON.parseObject(result).getString("error_description");
        } catch (Exception e) {
            log.error("易联云打印机打印logo设置失败，终端号 ： " + machineCode + ",logo ： " + imgUrl);
            return "易联云打印机打印logo设置失败";
        }
    }

    /**
     * 方法描述 : 删除打印设备logo
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:39
     * @param machineCode 请求参数
     * @return: java.lang.String
     * @Since version-1.4.0
     */
    public String printDeleteIcon(String machineCode) {
        try {
            String result = RequestMethod.getInstance().printDeleteIcon(ylyService.getAppToken(), machineCode);
            this.handelResultJson(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方法描述 : 订单打印【文本打印】
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:27
     * @param machineCode  易联云打印机终端号
     * @param content   打印内容
     * @param originId 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母 ，且在同一个client_id下唯一
     * @return: java.lang.Boolean
     * @Since version-1.4.0
     */
    public Boolean printIndex( String machineCode, String content, String originId) {
        try {
            String result = RequestMethod.getInstance().printIndex(ylyService.getAppToken(), machineCode, content, originId);

            Boolean b = this.handelResultJson(result);
            //订单打印成功不需处理
            if (b){
                return Boolean.TRUE;
            }

            //打印失败、保存打印失败订单信息
            log.error("订单打印失败 ： "  +  result + ",订单信息" + content);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 方法描述 : 打印机提示音类型、音量大小调节
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 16:07
     * @param machineCode 易联云打印机终端号
     * @param responseType 蜂鸣器:buzzer;喇叭:horn
     * @param voice 音量设置 ：     0:静音 1:低音 2:中音 3:高音
     * @return: java.lang.Boolean
     * @Since version-1.4.0
     */
    public Boolean printerSetVoice(String machineCode, String responseType, String voice) {
        try {
            String result =RequestMethod.getInstance().printSetSound(ylyService.getAppToken(),
                    machineCode,responseType,voice);
            Boolean b = this.handelResultJson(result);
            //订单打印成功不需处理
            if (b){
                return Boolean.TRUE;
            }

            //打印失败、保存打印失败订单信息
            log.error("修改打印声音失败，设备编码 ： " + machineCode + "\r\n响应结果 ：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 方法描述 : 设置打印机语音提示[根据文本内容播报]
     *            注意: 仅支持K4-WA、K4-GAD、K4-WGEAD、k6型号（除k6-wh外）
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:58
     * @param machineCode
     * @param content
     * @param isFile
     * @param aid
     * @param originId 请求参数
     * @return: java.lang.Boolean
     * @Since version-1.4.0
     */
    public Boolean printerSetVoice(String machineCode, String content, String isFile, String aid, String originId) {
        try {
            String result =RequestMethod.getInstance().printerSetVoice(ylyService.getAppToken(),
                    machineCode,content,isFile,aid,originId);
            Boolean b = this.handelResultJson(result);
            //订单打印成功不需处理
            if (b){
                return Boolean.TRUE;
            }

            //打印失败、保存打印失败订单信息
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 方法描述 : 获取打印机当前状态
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 17:04
     * @param machineCode  设备号
     * @return: Integer 打印机状态 ： 0 ：离线, 1 ：在线, 2 ： 缺纸, 100 ：易联云接口调用失败
     * @Since version-1.4.0
     */
    public Integer printerGetPrintStatus(String machineCode) {
        try {
            String result = RequestMethod.getInstance().printerGetPrintStatus(ylyService.getAppToken(),machineCode);
            Boolean b = this.handelResultJson(result);
            //打印机当前状态
            if (b){
                JSONObject resultJson = JSON.parseObject(result);
                return resultJson.getJSONObject("body").getInteger("state");
            }
            log.error("打印机状态获取失败，设备号 ： "  +  machineCode + "授权code ： " + ylyService.getAppToken() + JSON.parseObject(result).getJSONObject("error_description"));
        } catch (Exception e) {
            log.error("打印机状态获取失败，设备号 ： "  +  machineCode + "授权code ： " + ylyService.getAppToken(), e);
        }
        return 100;
    }

    /**
     * 方法描述 : 处理返回结果
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:40
     * @param result 请求参数
     * @return:  Boolean true ： 请求成功
     * @Since version-1.4.0
     */
    private Boolean handelResultJson(String result){
        JSONObject resultJson = JSON.parseObject(result);
        String error = resultJson.getString("error");
        //返回结果不为0，添加失败
        if (!Objects.equals(error,"0")){
            log.error("易联云打印机添加失败，原因" + resultJson);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
