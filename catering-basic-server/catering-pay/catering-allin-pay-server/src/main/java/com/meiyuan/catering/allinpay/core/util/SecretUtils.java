package com.meiyuan.catering.allinpay.core.util;

/**
 * @author zengzhangni
 * @date 2020/9/26 14:07
 * @since v1.1.0
 */

import jodd.util.ResourcesUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Slf4j
public class SecretUtils {
    private static Provider provider = new BouncyCastleProvider();

    private final static String PREFIX = "classpath:";

    private SecretUtils() {
    }

    public static String sign(PrivateKey privateKey, String text, String algorithm) throws Exception {
        String signedValue = md5(text);
        Signature signature = Signature.getInstance(algorithm, provider);
        signature.initSign(privateKey);
        signature.update(signedValue.getBytes(StandardCharsets.UTF_8));
        byte[] data = signature.sign();
        return Base64.encode(data);
    }


    public static String htSign(PrivateKey privateKey, String text) throws Exception {
        Signature signature = Signature.getInstance("SHA1WithRSA", provider);
        signature.initSign(privateKey);
        signature.update(text.getBytes(StandardCharsets.UTF_8));
        byte[] data = signature.sign();
        return Base64.encode(data);
    }

    public static boolean verify(PublicKey publicKey, String text, String sign) throws Exception {
        if (OpenUtils.isEmpty(sign)) {
            return false;
        } else {
            String signedValue = md5(text);
            Signature signature = Signature.getInstance("SHA256WithRSA", provider);
            signature.initVerify(publicKey);
            signature.update(signedValue.getBytes(StandardCharsets.UTF_8));
            byte[] signed = Base64.decode(sign);
            return signature.verify(signed);
        }
    }

    public static String md5(String src) {
        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = Base64.encode(md.digest(src.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
        }

        return result;
    }

    public static PrivateKey loadPrivateKey(String alias, String path, String password) throws Exception {
        InputStream ksfis = null;
        PrivateKey privateKey;
        try {
            log.info("path:{}", path);
            if (path.startsWith(PREFIX)) {
                ksfis = ResourcesUtil.getResourceAsStream(path.replace(PREFIX, ""));
            } else {
                ksfis = new FileInputStream(path);
            }

            KeyStore ks = KeyStore.getInstance("pkcs12");
            char[] storePwd = password.toCharArray();
            char[] keyPwd = password.toCharArray();
            ks.load(ksfis, storePwd);
            if (OpenUtils.isEmpty(alias)) {
                Enumeration<String> aliases = ks.aliases();
                if (aliases.hasMoreElements()) {
                    alias = aliases.nextElement();
                }
            }

            privateKey = (PrivateKey) ks.getKey(alias, keyPwd);
        } finally {
            if (ksfis != null) {
                ksfis.close();
            }
        }

        return privateKey;
    }

    public static PublicKey loadTLPublicKey(String certPath) throws Exception {
        InputStream certInputStream = null;
        PublicKey pubKey;
        CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");

        try {
            log.info("path:{}", certPath);
            if (certPath.startsWith(PREFIX)) {
                certInputStream = ResourcesUtil.getResourceAsStream(certPath.replace(PREFIX, ""));
            } else {
                certInputStream = new FileInputStream(certPath);
            }
            X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(certInputStream);
            pubKey = cert.getPublicKey();
        } finally {
            if (certInputStream != null) {
                certInputStream.close();
            }

        }

        return pubKey;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        return (byte) (b0 ^ b1);
    }

    public static String encryptAES(String plainText, String privateKey) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(privateKey.getBytes());
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = plainText.getBytes(StandardCharsets.UTF_8);
            cipher.init(1, secretKeySpec);
            byte[] byteRresult = cipher.doFinal(byteContent);
            String sb = "";

            for (byte element : byteRresult) {
                String hex = Integer.toHexString(element & 255);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb = sb.concat(hex.toUpperCase());
            }

            return sb;
        } catch (Exception var16) {
            return null;
        }
    }

    public static String decryptAES(String cipherText, String privateKey) {
        try {
            if (cipherText.length() < 1) {
                return null;
            } else {
                byte[] byteRresult = new byte[cipherText.length() / 2];

                for (int i = 0; i < cipherText.length() / 2; ++i) {
                    int high = Integer.parseInt(cipherText.substring(i * 2, i * 2 + 1), 16);
                    int low = Integer.parseInt(cipherText.substring(i * 2 + 1, i * 2 + 2), 16);
                    byteRresult[i] = (byte) (high * 16 + low);
                }

                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(privateKey.getBytes());
                kgen.init(128, random);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(2, secretKeySpec);
                byte[] result = cipher.doFinal(byteRresult);
                return new String(result);
            }
        } catch (Exception var10) {
            return null;
        }
    }
}
