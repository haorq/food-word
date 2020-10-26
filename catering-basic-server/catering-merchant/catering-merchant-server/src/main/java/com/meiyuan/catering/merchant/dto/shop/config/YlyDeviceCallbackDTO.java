package com.meiyuan.catering.merchant.dto.shop.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/9/27 0027 17:45
 * @Description 简单描述 :  打印机打印配置修改
 * @Since version-1.5.0
 */
@ApiModel("Yly打印机打印状态改变回调")
@Data
public class YlyDeviceCallbackDTO {
        @ApiModelProperty(value = "易联云打印机终端号")
        private String machine_code;

        @ApiModelProperty(value = "终端状态，1:在线，2:缺纸，0：离线(离线状态仅k4才会触发)")
        private String online;

        @ApiModelProperty(value = "推送的时间戳")
        private String push_time;

        /**
         *  应用将接收到易联云推送的POST Body
         *  得到的POST Body里面的sign
         *  对上一步得到的sign与md5(client_id+push_time+client_secret)校验是否正确
         *  通过上一步验证,请返回以下示例(格式json),否则将视为推送失败,会再次推送两次,如果均未有返回将放弃推送
         *  {"data": "OK"}
         */
        @ApiModelProperty(value = "签名 :")
        private String sign;

        @ApiModelProperty(value = "oauth_printStatus")
        private String cmd;
}







