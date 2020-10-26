package com.meiyuan.catering.user.vo.user;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/14 11:47
 */
@Data
public class UserPublicTextMessageVO {
    @ApiModelProperty("开发者微信号")
    @XStreamAlias(value = "ToUserName")
    private String toUserName;

    @ApiModelProperty("发送方帐号（一个OpenID）")
    @XStreamAlias(value = "FromUserName")
    private String fromUserName;

    @ApiModelProperty("消息创建时间 （整型）")
    @XStreamAlias(value = "CreateTime")
    private Long createTime;

    @ApiModelProperty("消息类型，event")
    @XStreamAlias(value = "MsgType")
    private String messageType;

    @ApiModelProperty("事件类型，subscribe(订阅)、unsubscribe(取消订阅)")
    @XStreamAlias(value = "Event")
    private String event;

    @ApiModelProperty("消息内容")
    @XStreamAlias(value = "Content")
    private String content;

    @ApiModelProperty("消息id")
    @XStreamAlias(value = "MsgId")
    private String msgId;
}
