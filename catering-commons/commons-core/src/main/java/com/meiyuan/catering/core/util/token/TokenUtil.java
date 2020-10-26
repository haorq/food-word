package com.meiyuan.catering.core.util.token;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.enums.base.TokenEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengzhangni
 * @date 2020/4/17
 */
@Slf4j
public class TokenUtil {

    public static final String INFO = "info";

    public static String generateIdToken(Long id, TokenEnum tokenEnum) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", id);
        return generateToken(map, tokenEnum);
    }


    /**
     * 方法描述 : pc端修改密码退出登录token获取
     * @Author: MeiTao
     * @Date: 2020/10/13 0013 8:56
     * @param accountTypeId
     * @param accountType
     * @param logBackType LogBackTypeEnum 退出登录类型
     * @return: java.lang.String
     * @Since version-1.5.0
     */
    public static String generatePcToken(Long accountTypeId,Integer accountType,Integer logBackType) {
        Map<String, Object> map = new HashMap<>(3);
        //账号类型，账号类型对应id
        map.put("accountTypeId", accountTypeId);
        map.put("accountType", accountType);
        map.put("logBackType", logBackType);
        String token = TokenUtil.generateToken(map, TokenEnum.MERCHANT);
        return token;
    }

    /**
     * 方法描述 : pc端登陆token生成
     * @Author: MeiTao
     * @Date: 2020/10/13 0013 8:51
     * @param map
     * @param tokenEnum 请求参数
     * @return: java.lang.String
     * @Since version-1.5.0
     */
    public static String generateToken(Map<String, Object> map, TokenEnum tokenEnum) {
        String token = null;
        try {
            token = JwtHelper.generateToken(map, tokenEnum.getPrivateKey(), tokenEnum.getExpirationTime());
        } catch (Exception e) {
            log.error("创建token失败");
        }
        return token;
    }


    public static <T> T getFromToken(String token, TokenEnum tokenEnum, Class<T> t) throws Exception {
        Map<String, Object> map = getInfoFromToken(token, tokenEnum.getPublicKey());
        String string = JSONObject.toJSONString(map);

        return JSONObject.parseObject(string, t);
    }

    public static Map<String, Object> getInfoFromToken(String token, String pubKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKey);
        Claims body = claimsJws.getBody();
        body.remove("exp");
        return body;
    }

    private static Jws<Claims> parserToken(String token, String pubKey) throws Exception {
        byte[] bytes = toBytes(pubKey);
        return Jwts.parser().setSigningKey(getPublicKey(bytes)).parseClaimsJws(token);
    }

    private static PublicKey getPublicKey(byte[] publicKey) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private static byte[] toBytes(String s) throws IOException {
        return (new BASE64Decoder()).decodeBuffer(s);
    }
}
