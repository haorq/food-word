

package com.meiyuan.catering.core.util.token;

import io.jsonwebtoken.*;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zengzhangni
 * @date 2019/3/21
 */
public class JwtHelper {
    private static RsaKeyHelper rsaKeyHelper = new RsaKeyHelper();

    /**
     * 密钥加密token
     *
     * @param priKey
     * @param expire
     * @return
     * @throws Exception
     */
    public static String generateToken(Map<String, Object> map, String priKey, int expire) throws Exception {
        JwtBuilder builder = Jwts.builder();
        if (map != null) {
            builder.setClaims(map);
        }
        return builder
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(RsaKeyHelper.toBytes(priKey)))
                .compact();
    }


    /**
     * 公钥解析token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Jws<Claims> parserToken(String token, String pubKey) throws Exception {
        byte[] bytes = RsaKeyHelper.toBytes(pubKey);
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(bytes)).parseClaimsJws(token);
        return claimsJws;
    }


    /**
     * 获取token中的用户信息
     *
     * @param token
     * @param pubKey
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getInfoFromToken(String token, String pubKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKey);
        Claims body = claimsJws.getBody();
        body.remove("exp");
        return body;
    }
}
