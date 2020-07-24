package com.ftf.coral.util.cipher;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Hex;

public class HmacUtils {

    /**
     * 构建Hmac密钥
     * 
     * @param hmacType
     *            Hmac类型
     * @return 密钥（16进制）
     */
    public static String generateKeyHex(HmacType hmacType) {
        byte[] key = generateKey(hmacType);
        return Hex.encodeHexString(key);
    }

    /**
     * 构建Hmac密钥
     * 
     * @param hmacType
     *            Hmac类型
     * @return 密钥
     */
    public static byte[] generateKey(HmacType hmacType) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(hmacType.getType());
            // 产生密钥
            SecretKey secretKey = keyGenerator.generateKey();
            // 获得密钥
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new CipherException(e);
        }
    }
    
    public static void main(String[] args) {
        System.out.println(HmacUtils.generateKey(HmacType.HmacSHA384));
    }
}
