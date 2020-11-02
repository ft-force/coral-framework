package com.ftf.coral.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftf.coral.CoralCore;

public class AESUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtils.class);

    private static final String defaultKey = "vdXxGkQMM&^%Th(_\6?KH";

    public static String encode(String in, String key) throws InvalidKeyException, NoSuchAlgorithmException,
        NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        if (StringUtils.isBlank(key))
            throw new IllegalArgumentException("key is blank");
        String hex = "";
        byte[] bytIn = in.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] bytOut = cipher.doFinal(bytIn);
        hex = byte2hexString(bytOut);

        return hex;
    }

    public static String encode(String in) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException {
        return encode(in, defaultKey);
    }

    public static String decode(String hex, String key) throws InvalidKeyException, NoSuchAlgorithmException,
        NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        String rr = "";

        byte[] bytIn = hex2Bin(hex);

        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] bytOut = cipher.doFinal(bytIn);
        rr = new String(bytOut);

        return rr;
    }

    /**
     * 采用AES方式加密 (算法AES/模式CBC/补码方式PKCS5Padding)
     *
     * @param src
     *            源字符串
     * @param key
     *            密钥
     * @param iv
     *            向量，使用CBC模式，需要一个向量iv，可增加加密算法的强度
     * @return 加密后的字符串
     */
    public static String encryptAesCbc(String src, String key, String iv) {

        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] raw = key.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec ivp = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivp);

            byte[] encryptedBytes = cipher.doFinal(src.getBytes(CoralCore.encoding));

            // 此处使用BASE64做转码功能，同时能起到2次加密的作用
            String encrypted = Base64.getEncoder().encodeToString(encryptedBytes);
            return encrypted;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
            | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
            | UnsupportedEncodingException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * 采用AES方式解密 (算法AES/模式CBC/补码方式PKCS5Padding)
     *
     * @param encrypted
     *            加密后的字符串
     * @param key
     *            密钥
     * @param iv
     *            向量，使用CBC模式，需要一个向量iv，可增加加密算法的强度
     * @return 解密后的字符串
     */
    public static String decryptAesCbc(String encrypted, String key, String iv) {

        try {

            byte[] raw = key.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            // 算法/模式/补码方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivp = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivp);

            // 先用base64解码
            byte[] decodeBytes = Base64.getDecoder().decode(encrypted);
            byte[] srcBytes = cipher.doFinal(decodeBytes);

            String srcStr = new String(srcBytes, CoralCore.encoding);
            return srcStr;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
            | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
            | UnsupportedEncodingException e) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("解码失败 encrypted:{} | key:{} | iv:{}", encrypted, key, iv);
            }

            throw new RuntimeException(e);
        }
    }

    public static String decode(String in) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException {
        return decode(in, defaultKey);
    }

    private static byte[] hex2Bin(String src) {
        if (src.length() < 1)
            return null;
        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
            encrypted[i] = (byte)(high * 16 + low);
        }
        return encrypted;
    }

    private static String byte2hexString(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            strbuf.append(Integer.toString((buf[i] >> 4) & 0xf, 16) + Integer.toString(buf[i] & 0xf, 16));
        }

        return strbuf.toString();
    }

    static String ipPrefix = Hex.encodeHexString("IPPFX:".getBytes());
    static String noPrefix = Hex.encodeHexString("NOPFX:".getBytes());

    public static String decodePassword(String cipherPwd) {

        String subStr = cipherPwd.substring(noPrefix.length());
        try {
            byte[] data = EncryptUtils.decryptByPrivateKey(Hex.decodeHex(subStr.toCharArray()),
                EncryptUtils.getPrivateKey("UMPassword"));
            return new String(data);
        } catch (Exception e) {
            System.exit(2);
        }
        return null;

    }

    static class EncryptUtils {

        public static final String rsaPrvKey =
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAItPcRRK2Tbz6piwnZCNSLEfWRX59q1eyB0Y9JgNtPs1f+Wkxm/2thT5iGqVLLNFEUbhELnxb89xk4heJzTqChGESYH/ZOs+HKJ8U3+Ttp0ZE6MY53pH8JzHqXnaePeDqX5tJtSxxWfD7wqkM1UglW6Fs8EnLP5k9zlriDIXMFvhAgMBAAECgYANcM21Kn+IiMICl0+saaUwyZh7wVEmavWdsRGwNepXLlM3oc0vcjshDO43cksMxMYk84P8nKmv9wJH7uWTel0cLOGkWCGtrSqPszJkx0Y9SHL6l9Z4yCk/TtGtBm/nP9Q3zM6ev1pfeTCtRxzK5Mb47e+WTNwVCJm0FrSoyIzw/QJBANKpaTy35+P6wZQMhnWNFQBN0PlnVPmTblMJDJW/cl+tfk9w15fFcT+rRwsWa/r2PKEiFzHg0kKAgaP6ePWtZhMCQQCpSty6LDDdqlNVbTwEFnrpG9qsWkPjhG9ajbpkTrpPnE3PT5MGw3t0GrDxK2elZ5PVutRpPLXs1DcYS4wkJ4S7AkEA0NETOxfFKixPJHUB95YAokuAgSiXh8lHi9Glgu7B7etpEE/3tT8HEiiyhGAWay8YTFUhjtSfN0Jwv12x9z2JtwJAbqEASx0TtddXa8zdWmKCYZEVPmoiUSy7Q/a4JlKYR+wBoQcEMnhOVZoXpRJTQfDE1/emVTsaO7CWbGb6Jqo4fwJAeBJFDV4qrNkreIrfpuIHUaire+0dPwer8X14iFxzf/P1oxLT/Nv2sWMNKQYTmGPmLt4IKeTb2WqOqNYvl1PGkg==";
        public static final PrivateKey RsaPrvKey = getRSAPrivateKey(rsaPrvKey);
        public static final String rsaPubKey =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLT3EUStk28+qYsJ2QjUixH1kV+fatXsgdGPSYDbT7NX/lpMZv9rYU+YhqlSyzRRFG4RC58W/PcZOIXic06goRhEmB/2TrPhyifFN/k7adGROjGOd6R/Ccx6l52nj3g6l+bSbUscVnw+8KpDNVIJVuhbPBJyz+ZPc5a4gyFzBb4QIDAQAB";
        public static final PublicKey RsaPubKey = getRSAPublicKey(rsaPubKey);

        static KeyFactory rsaKeyFactory;
        static {
            try {
                rsaKeyFactory = KeyFactory.getInstance("RSA");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        public static PublicKey getPublicKey(String keyName) {
            return RsaPubKey;
        }

        public static PrivateKey getPrivateKey(String keyName) {
            return RsaPrvKey;
        }

        public static PublicKey getRSAPublicKey(String publicKey) {
            try {
                byte[] keyBytes = Base64.getDecoder().decode(publicKey);

                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                PublicKey pubKey = keyFactory.generatePublic(keySpec);
                return pubKey;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        public static PrivateKey getRSAPrivateKey(String privateKey) {
            try {
                byte[] keyBytes = Base64.getDecoder().decode(privateKey);
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
                return priKey;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static String sign(byte[] data, PrivateKey priKey) {
            try {
                Signature signature = Signature.getInstance("MD5withRSA");
                signature.initSign(priKey);
                signature.update(data);
                return Hex.encodeHexString(signature.sign());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static byte[] decryptByPrivateKey(byte[] data, Key privateKey) {
            try {
                Cipher cipher = Cipher.getInstance(rsaKeyFactory.getAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
