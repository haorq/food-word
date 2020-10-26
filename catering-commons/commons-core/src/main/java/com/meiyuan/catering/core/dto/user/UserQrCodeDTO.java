package com.meiyuan.catering.core.dto.user;

import cn.binarywang.wx.miniapp.util.json.WxMaGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/10 14:01
 */
@Data
public class UserQrCodeDTO implements Serializable {
    private static final long serialVersionUID = 2020091014051105001L;

    /**
     * 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     */
    @SerializedName("ticket")
    private String ticket;
    /**
     * 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）。
     */
    @SerializedName("expire_seconds")
    private Integer expireSeconds;
    /**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    @SerializedName("url")
    private String url;

    public static UserQrCodeDTO fromJson(String json) {
        return WxMaGsonBuilder.create().fromJson(json, UserQrCodeDTO.class);
    }
}
