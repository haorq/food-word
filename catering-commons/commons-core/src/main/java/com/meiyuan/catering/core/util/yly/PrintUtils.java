package com.meiyuan.catering.core.util.yly;

import com.google.gson.Gson;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author lh
 * @date 2020/09/24
 * 易联云API文档：http://doc2.10ss.net/331992
 * 自有应用服务模式
 */
@Component
@Slf4j
public class PrintUtils {

    private static final String YLY_RET = "0";

    @Resource
    private YlyService ylyService;

    public void setIcon(String printKey, String logo) {
        String ylyToken = ylyService.getAppToken();
        if (StringUtils.isBlank(ylyToken)) {
            // - 第一次获取token，将token存储，该token永久有效
            String token = Methods.getInstance().getFreedomToken();
            Methods.getInstance().refreshToken();
        }
        Methods.token = ylyToken;
        Methods.getInstance().init(ylyService.getAppId(), ylyService.getAppSecret());
        if (StringUtils.isNotBlank(logo)) {
            // 设置LOGO
            String ret = Methods.getInstance().setIcon(printKey, logo);
            log.info("设备logo设置成功，设备号 ： " + printKey );
            log.info("响应结果 ： " + ret );
        }
    }

    /**
     * 方法描述 : 打印订单
     * @Author: lh
     * @Date: 2020/9/26 0026 14:54
     * @param printKey  设备号（machine_code）
     * @param content 打印内容
     * @param orderNo 订单号
     * @param shopName 店铺名称
     * @Since version-1.4.0
     */
    public void printOrder(
            String printKey,
            String content,
            String orderNo,
            String shopName) {
        String ret = Methods.getInstance().print(printKey, content, orderNo, shopName);
        HttpResponseWithPrinter httpResponseWithPrinter = (HttpResponseWithPrinter) JsonUtil.fromJson(ret, HttpResponseWithPrinter.class);
        if (!httpResponseWithPrinter.getError().equals(YLY_RET)) {
            throw new CustomException(ErrorCode.PRINT_ERROR, ErrorCode.PRINT_ERROR_MSG);
        }
    }


    public void printTest(String printKey) {
        String content = buildTitle("打印机联网成功，可正常使用～");
        String ret = Methods.getInstance().print(printKey, content, uuid(), "");
        HttpResponseWithPrinter httpResponseWithPrinter = (HttpResponseWithPrinter) JsonUtil.fromJson(ret, HttpResponseWithPrinter.class);
        if (!httpResponseWithPrinter.getError().equals(YLY_RET)) {
            throw new CustomException(ErrorCode.PRINT_ERROR, ErrorCode.PRINT_ERROR_MSG);
        }
    }

    private String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * 配置打印机到易联云后台
     *
     * @param printKey
     * @param printSecret
     * @param printName
     * @param logo
     */
    /**
     * 方法描述 :
     * @Author: MeiTao
     * @Date: 2020/9/26 0026 15:04
     * @param printKey
     * @param printSecret
     * @param printName
     * @param logo 请求参数
     * @return: void
     * @Since version-1.4.0
     */
    public void addPrinter(String printKey, String printSecret, String printName, String logo) {
        this.setIcon(printKey, logo);
        String ret = Methods.getInstance().addPrinter(printKey, printSecret, printName);
        HttpResponseWithPrinter httpResponseWithPrinter = (HttpResponseWithPrinter) JsonUtil.fromJson(ret, HttpResponseWithPrinter.class);
        if (!httpResponseWithPrinter.getError().equals(YLY_RET)) {
            throw new CustomException(ErrorCode.PRINT_ERROR, ErrorCode.PRINT_ERROR_MSG);
        }
    }


    /**
     * 从易联云后台删除打印机
     *
     * @param printKey
     */
    public void del(String printKey) {
        String ret = Methods.getInstance().deletePrinter(printKey);
        HttpResponseWithPrinter httpResponseWithPrinter = (HttpResponseWithPrinter) JsonUtil.fromJson(ret, HttpResponseWithPrinter.class);
        if (!httpResponseWithPrinter.getError().equals(YLY_RET)) {
            throw new CustomException(ErrorCode.PRINT_ERROR, ErrorCode.PRINT_ERROR_MSG);
        }
    }



    private String buildTitle(String title) {
        StringBuffer temp = new StringBuffer();
        // - 标题
        temp.append("<FB>");
        temp.append("<center>");
        temp.append(title);
        temp.append("</center>");
        temp.append("</FB>");
        temp.append("\r");
        return temp.toString();
    }

}
