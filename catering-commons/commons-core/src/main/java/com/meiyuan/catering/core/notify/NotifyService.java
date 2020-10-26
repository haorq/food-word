package com.meiyuan.catering.core.notify;

import com.google.common.base.Strings;
import com.meiyuan.catering.core.config.SmsProperties;
import com.meiyuan.catering.core.util.HttpUtil;
import com.meiyuan.msg.pojo.*;
import com.meiyuan.msg.request.ParamRequest;
import com.meiyuan.msg.service.ISendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * 商城通知服务类
 * @author admin
 */
@Slf4j
@Service
public class NotifyService {
    @Autowired
    private ISendMessageService sendMessageService;
    @Autowired
    private SmsProperties smsProperties;

    /**
     * 支付成功通知
     */
    @Async
    public void paySmsTemplate(String phoneNumber, NotifyType notifyType, String[] paramsStr) {
        if (smsProperties.getPay()) {
            notifySmsTemplateSync(phoneNumber, notifyType, paramsStr);
        }
    }

    /**
     * 短信模版消息通知
     *
     * @param phoneNumber 接收通知的电话号码
     * @param notifyType  通知类别，通过该枚举值在配置文件中获取相应的模版ID
     * @param paramsStr   通知模版内容里的参数，类似"您的验证码为{1}"中{1}的值
     */
    @Async
    public void notifySmsTemplate(String phoneNumber, NotifyType notifyType, String[] paramsStr) {
        notifySmsTemplateSync(phoneNumber, notifyType, paramsStr);
    }

    /**
     * 以同步的方式发送短信模版消息通知
     *
     * @param phoneNumber 接收通知的电话号码
     * @param notifyType  通知类别，通过该枚举值在配置文件中获取相应的模版ID
     * @param paramsStr   通知模版内容里的参数，类似"您的验证码为{1}"中{1}的值
     * @return
     */
    public SmsResult notifySmsTemplateSync(String phoneNumber, NotifyType notifyType, String[] paramsStr) {
        String code = code(notifyType);
        LinkedHashMap<String, String> map = getShortSms(notifyType, paramsStr);
        List<ParamRequest> params = new ArrayList<ParamRequest>();
        map.forEach((k, v) -> {
            params.add(ParamRequest.builder()
                    .key(k)
                    .value(v)
                    .build());
        });
        ShortMessage shortMessage = ShortMessage.builder()
                .code(code)
                .params(params)
                .phone(phoneNumber)
                .build();
        shortMessage.setIp("127.0.0.1");
        Result aliContent = null;
        try {
            aliContent = sendMessageService.sendAliShortMsg(shortMessage);
        } catch (Exception e) {
            log.error("send short msg error:{}", e.getMessage());
        }
        log.info("send msg result:{}", aliContent);

        SmsResult smsResult = new SmsResult();
        smsResult.setSuccessful(true);
        return smsResult;
    }

    /**
     * 微信模版消息通知,不跳转
     * <p>
     * 该方法会尝试从数据库获取缓存的FormId去发送消息
     *
     * @param touser     接收者openId
     * @param notifyType 通知类别，通过该枚举值在配置文件中获取相应的模版ID
     * @param params     通知模版内容里的参数，类似"您的验证码为{1}"中{1}的值
     */
    @Async
    public void notifyWxTemplate(String touser, NotifyType notifyType, String[] params) {
        notifyWxTemplate(touser, notifyType, params, null);
    }

    /**
     * 微信模版消息通知，带跳转
     * <p>
     * 该方法会尝试从数据库获取缓存的FormId去发送消息
     *
     * @param touser     接收者openId
     * @param notifyType 通知类别，通过该枚举值在配置文件中获取相应的模版ID
     * @param params     通知模版内容里的参数，类似"您的验证码为{1}"中{1}的值
     * @param page       点击消息跳转的页面
     */
    @Async
    public void notifyWxTemplate(String touser, NotifyType notifyType, String[] params, String page) {
        Map<String, Object> data = getWxTemplateMsg(notifyType, params);
        WxPublicTemplateMessage message = WxPublicTemplateMessage.builder()
                .code(code(notifyType))
                .openId(touser)
                .data(data)
                .build();
        if (!StringUtils.isEmpty(page)) {
            message.setUrl(page);
        }
        message.setIp("127.0.0.1");
        message.setAppId("10075");

        try {
            System.out.println("--" + sendMessageService.sendWxPublicTemplateMsg(message));
        } catch (IOException e) {
            log.error("send wx template error:{}", e.getMessage());
        }
    }

    /**
     * 邮件消息通知, 接收者在spring.mail.sendto中指定
     *
     * @param subject 邮件标题
     * @param content 邮件内容
     */
    @Async
    public void notifyMail(String subject, String content) {
        notifySslMail(subject, content);
    }

    /**
     * 发送ssl邮件
     *
     * @param subject 邮件标题
     * @param content 邮件内容
     */
    @Async
    public void notifySslMail(String subject, String content) {
        MailMessage message = MailMessage.builder()
                //.addresses(new String[]{"297750341@qq.com"})
                .subject(subject)
                .content(content)
                .build();
        message.setIp("127.0.0.1");
        message.setAppId("10047");

        try {
            System.out.println(sendMessageService.sendWxMail(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送微信小程序订阅消息
     *
     * @param openId 微信openId
     */
    public void sendWxMiniMsg(String openId) {
        WxMiniMessage wxMiniMessage = getWxMiniMessage(openId);
        try {
            sendMessageService.sendWxMiniMsg(wxMiniMessage);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    private WxMiniMessage getWxMiniMessage(String openId) {
        Map<String, Object> data = new HashMap<>(16);
        //todo 根据不同的场景构建不同的data数据
        WxMiniMessage message = WxMiniMessage.builder()
//                .code() //todo 根据不同的场景使用不同的code值
                .openId("oeI8R5axiaZHm5vLbh1y5msswyWk")
                .data(data)
                .build();
        message.setIp("127.0.0.1");
        //todo 根据不同的场景使用不同的appId值
        message.setAppId("10118");
        return message;
    }

    public boolean isSmsEnable() {
        return true;
    }

    private LinkedHashMap getShortSms(NotifyType notifyType, String[] params) {
        LinkedHashMap map = new LinkedHashMap();
        String shortUrl = "";
        switch (notifyType) {
            case VERIFY_CODE_NOTIFY:
                map.put("code", params[0]);
                break;
            case CREATE_ACCOUNT_SUCCESS_NOTIFY:
                map.put("account", params[0]);
                map.put("password", params[1]);
                break;
            case UPDATE_PRINT_STATUS_NOTIFY:
                map.put("printName", params[0]);
                map.put("reason", params[1]);
                break;
            case UPDATE_ACCOUNT_SUCCESS_NOTIFY:
                map.put("account", params[0]);
                map.put("password", params[1]);
                break;
            case CREATE_MERCHANT_SUCCESS_NOTIFY:
                map.put("account", params[0]);
                map.put("password", params[1]);
                break;
            case CREATE_ENTERPRISE_ACCOUNT_SUCCESS_NOTIFY:
                map.put("account", params[0]);
                map.put("password", params[1]);
                break;
            case MERCHANT_CANCEL_ORDER_NOTIFY:
                map.put("dateTime", params[0]);
                break;
            case SELF_PICKUP_NOTIFY:
                map.put("code", params[0]);
                map.put("pickupPoint", params[1]);
                map.put("orderId", params[2]);
                map.put("codeNum", params[0]);
                break;
            case HOME_DELIVERY_NOTIFY:
                map.put("code", params[0]);
                map.put("orderId", params[1]);
                map.put("codeNum", params[0]);
                break;
            case SELF_PICKUP_ONE_HOUR_NOTIFY:
                map.put("beginTime", params[0]);
                map.put("endTime", params[1]);
                map.put("code", params[2]);
                map.put("pickupPoint", params[3]);
                map.put("orderId", params[4]);
                map.put("codeNum", params[2]);
                break;
            case GROUP_ORDER_SELF_PICKUP_NOTIFY:
                map.put("code", params[0]);
                map.put("pickupPoint", params[1]);
                map.put("orderId", params[2]);
                map.put("codeNum", params[0]);
                break;
            case GROUP_ORDER_HOME_DELIVERY_NOTIFY:
                map.put("code", params[0]);
                map.put("orderId", params[1]);
                map.put("codeNum", params[0]);
                break;
            case MERCHANT_APPLY_REJECT_NOTIFY:
                map.put("TXT_30", params[0]);
                break;
            default:
                break;
        }
        return map;
    }

    private LinkedHashMap getWxTemplateMsg(NotifyType notifyType, String[] params) {
        LinkedHashMap map = new LinkedHashMap();

        return map;
    }

    private String code(NotifyType notifyType) {
        return notifyType.getType();
    }

    @Value("${message.pickupPointCodeUrl}")
    private String pickupPointCodeUrl;

    @Value("${message.appDownloadUrl}")
    private String appDownloadUrl;

    @Value("${message.baiduShortUrl}")
    private String baiduShortUrl;

    @Value("${message.baiduShortUrlToken}")
    private String baiduShortUrlToken;

    /**
     * 构造取餐码地址
     *
     * @param orderId 订单ID
     * @param code    取餐码
     * @return
     */
    private String pickupPointCodeNumShortUrl(String orderId, String code) {
        if (Strings.isNullOrEmpty(orderId) || Strings.isNullOrEmpty(code)) {
            return "";
        }
        String codeNumUrl = pickupPointCodeUrl + "?id=" + orderId + "&code=" + code;
        return shortUrl(codeNumUrl);
    }

    private String shortUrl(String longUrl) {
        com.meiyuan.catering.core.util.Result<String> result = HttpUtil.createShortUrl(longUrl, "1-year", baiduShortUrl, baiduShortUrlToken);
        if (result.success()) {
            return result.getData();
        }
        return "";
    }
}
