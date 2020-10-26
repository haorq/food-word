package com.meiyuan.catering.wx.utils;

import com.meiyuan.catering.user.vo.user.UserPublicTextMessageVO;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/14 11:36
 */
public class WxMessageUtil {
    public static final String MSGTYPE_EVENT = "event";//消息类型--事件
    public static final String MESSAGE_SUBSCIBE = "subscribe";//消息事件类型--订阅事件
    public static final String MESSAGE_UNSUBSCIBE = "unsubscribe";//消息事件类型--取消订阅事件
    public static final String MESSAGE_TEXT = "text";//消息类型--文本消息

    /*
     * 组装文本消息
     */
    public static String textMsg(String toUserName,String fromUserName,String content){
        UserPublicTextMessageVO text = new UserPublicTextMessageVO();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMessageType(MESSAGE_TEXT);
        text.setCreateTime(System.currentTimeMillis());
        text.setContent(content);
        return WxXmlUtil.textMsgToxml(text);
    }

    /*
     * 响应订阅事件--回复文本消息
     */
    public static String subscribeForText(String toUserName,String fromUserName){
        return textMsg(toUserName, fromUserName, "欢迎关注，精彩内容不容错过！！！");
    }

    /*
     * 响应取消订阅事件
     */
    public static String unsubscribe(String toUserName,String fromUserName){
        //TODO 可以进行取关后的其他后续业务处理
        System.out.println("用户："+ fromUserName +"取消关注~");
        return "";
    }
}
