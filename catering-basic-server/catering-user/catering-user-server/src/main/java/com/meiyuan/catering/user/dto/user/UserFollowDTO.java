package com.meiyuan.catering.user.dto.user;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/3 15:16
 */
@Data
@Accessors(chain = true)
@XStreamAlias(value = "xml")
@ApiModel("用户关注公众号上传参数")
public class UserFollowDTO {

    @ApiModelProperty("开发者微信号")
    @XStreamAlias(value = "ToUserName")
    private String toUserName;

    @ApiModelProperty("发送方帐号（一个OpenID）")
    @XStreamAlias(value = "FromUserName")
    private String fromUserName;

    @ApiModelProperty("消息创建时间 （整型）")
    @XStreamAlias(value = "CreateTime")
    private Integer createTime;

    @ApiModelProperty("消息类型，event")
    @XStreamAlias(value = "MsgType")
    private String messageType;

    @ApiModelProperty("事件类型，subscribe(订阅)、unsubscribe(取消订阅)")
    @XStreamAlias(value = "Event")
    private String event;

    @ApiModelProperty("事件KEY值，qrscene_为前缀，后面为二维码的参数值")
    @XStreamAlias(value = "EventKey")
    private String eventKey;
}
