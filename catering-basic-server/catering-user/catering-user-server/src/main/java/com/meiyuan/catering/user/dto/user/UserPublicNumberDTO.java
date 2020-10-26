package com.meiyuan.catering.user.dto.user;

import cn.binarywang.wx.miniapp.util.json.WxMaGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * description：公众号用户信息
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/8 11:02
 */
@Data
public class UserPublicNumberDTO implements Serializable {
    private static final long serialVersionUID = -202009081110110501L;

    /** 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息*/
    @SerializedName("subscribe")
    private Integer subscribe;
    @SerializedName("openid")
    private String openid;
    @SerializedName("unionid")
    private String unionid;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("sex")
    private Integer sex;
    @SerializedName("city")
    private String city;
    @SerializedName("province")
    private String province;
    @SerializedName("country")
    private String country;
    @SerializedName("headimgurl")
    private String headimgurl;

    public static UserPublicNumberDTO fromJson(String json) {
        return WxMaGsonBuilder.create().fromJson(json, UserPublicNumberDTO.class);
    }
}
