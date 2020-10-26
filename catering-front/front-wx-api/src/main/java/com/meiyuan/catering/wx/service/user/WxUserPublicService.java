package com.meiyuan.catering.wx.service.user;

import com.google.common.collect.Lists;
import com.meiyuan.catering.core.config.WxProperties;
import com.meiyuan.catering.core.redis.utils.WxPublicUtil;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.user.dto.user.UserFollowDTO;
import com.meiyuan.catering.user.fegin.user.UserClient;
import com.meiyuan.catering.wx.utils.WxMessageUtil;
import com.meiyuan.catering.wx.utils.WxXmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/14 10:24
 */
@Service
@Slf4j
public class WxUserPublicService {

    @Autowired
    private WxProperties properties;

    @Resource
    private UserClient userClient;

    @Resource
    private WxPublicUtil wxPublicUtil;

    /**
     * describe: 公众号验证接口
     *
     * @param request
     * @param response
     * @author: yy
     * @date: 2020/9/14 9:19
     * @return: {@link String}
     * @version 1.4.0
     **/
    public String userFocusOn(HttpServletRequest request, HttpServletResponse response) {
        try {
            String echostr = request.getParameter("echostr");
            boolean checkBool = this.checkToken(request);
            if (!checkBool) {
                log.info("签名校验不通过！");
                return "";
            }
            //把微信返回的xml信息转义成map
            String xml = WxXmlUtil.getXml(request);
            log.info("后台接收到公众号发来的数据是:\n{}", xml);
            // 把微信发送给后端的xml消息转成 UserFollowDTO 对象
            UserFollowDTO userFollow = WxXmlUtil.fromXml(xml, UserFollowDTO.class);
            if (userFollow == null) {
                return echostr;
            }
            if (!WxMessageUtil.MSGTYPE_EVENT.equals(userFollow.getMessageType())) {//如果为事件类型
                return echostr;
            }
            if (WxMessageUtil.MESSAGE_UNSUBSCIBE.equals(userFollow.getEvent())) {//处理取消订阅事件
                log.info("用户取消关注了公众号");
                return echostr;
            }
            //处理订阅事件
            userClient.userFocusOn(userFollow, wxPublicUtil.getWxAccessToken());
            return echostr;
        } catch (Exception e) {
            log.error("公众号签名校验失败：", e);
            return "";
        }
    }

    /**
     * describe: 校验签名
     *
     * @param request
     * @author: yy
     * @date: 2020/9/14 15:06
     * @return: {@link boolean}
     * @version 1.4.0
     **/
    private boolean checkToken(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        ArrayList<String> arrayList = Lists.newArrayList();
        arrayList.add(signature);
        arrayList.add(timestamp);
        arrayList.add(nonce);
        //排序
        String sortString = this.sort(properties.getWxToken(), timestamp, nonce);
        //加密
        String myToken = this.shaDecode(sortString);
        //校验签名
        if (BaseUtil.isEmptyStr(myToken) || !myToken.equals(signature)) {
            return false;
        }
        return true;
    }

    /**
     * 排序方法
     *
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    private String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder builder = new StringBuilder();
        for (String str : strArray) {
            builder.append(str);
        }

        return builder.toString();
    }

    /**
     * describe: 公众号token解码
     *
     * @param explain
     * @author: yy
     * @date: 2020/9/14 10:02
     * @return: {@link String}
     * @version 1.4.0
     **/
    private String shaDecode(String explain) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(explain.getBytes());
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("公众号token验证解码错误：", e);
            return "";
        }
    }
}
