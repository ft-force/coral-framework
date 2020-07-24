package com.ftf.coral.util;

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
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class AESUtils {

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
            encrypted[i] = (byte) (high * 16 + low);
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

        public static final String rsaPrvKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAItPcRRK2Tbz6piwnZCNSLEfWRX59q1eyB0Y9JgNtPs1f+Wkxm/2thT5iGqVLLNFEUbhELnxb89xk4heJzTqChGESYH/ZOs+HKJ8U3+Ttp0ZE6MY53pH8JzHqXnaePeDqX5tJtSxxWfD7wqkM1UglW6Fs8EnLP5k9zlriDIXMFvhAgMBAAECgYANcM21Kn+IiMICl0+saaUwyZh7wVEmavWdsRGwNepXLlM3oc0vcjshDO43cksMxMYk84P8nKmv9wJH7uWTel0cLOGkWCGtrSqPszJkx0Y9SHL6l9Z4yCk/TtGtBm/nP9Q3zM6ev1pfeTCtRxzK5Mb47e+WTNwVCJm0FrSoyIzw/QJBANKpaTy35+P6wZQMhnWNFQBN0PlnVPmTblMJDJW/cl+tfk9w15fFcT+rRwsWa/r2PKEiFzHg0kKAgaP6ePWtZhMCQQCpSty6LDDdqlNVbTwEFnrpG9qsWkPjhG9ajbpkTrpPnE3PT5MGw3t0GrDxK2elZ5PVutRpPLXs1DcYS4wkJ4S7AkEA0NETOxfFKixPJHUB95YAokuAgSiXh8lHi9Glgu7B7etpEE/3tT8HEiiyhGAWay8YTFUhjtSfN0Jwv12x9z2JtwJAbqEASx0TtddXa8zdWmKCYZEVPmoiUSy7Q/a4JlKYR+wBoQcEMnhOVZoXpRJTQfDE1/emVTsaO7CWbGb6Jqo4fwJAeBJFDV4qrNkreIrfpuIHUaire+0dPwer8X14iFxzf/P1oxLT/Nv2sWMNKQYTmGPmLt4IKeTb2WqOqNYvl1PGkg==";
        public static final PrivateKey RsaPrvKey = getRSAPrivateKey(rsaPrvKey);
        public static final String rsaPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLT3EUStk28+qYsJ2QjUixH1kV+fatXsgdGPSYDbT7NX/lpMZv9rYU+YhqlSyzRRFG4RC58W/PcZOIXic06goRhEmB/2TrPhyifFN/k7adGROjGOd6R/Ccx6l52nj3g6l+bSbUscVnw+8KpDNVIJVuhbPBJyz+ZPc5a4gyFzBb4QIDAQAB";
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
                // 瑙ｅ瘑鐢眀ase64缂栫爜鐨勫叕閽�
                byte[] keyBytes = Base64.getDecoder().decode(publicKey);

                // 鏋勯�燲509EncodedKeySpec瀵硅薄
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

                // KEY_ALGORITHM 鎸囧畾鐨勫姞瀵嗙畻娉�
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                // 鍙栧叕閽ュ寵瀵硅薄
                PublicKey pubKey = keyFactory.generatePublic(keySpec);
                return pubKey;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        public static PrivateKey getRSAPrivateKey(String privateKey) {
            try {
                byte[] keyBytes = Base64.getDecoder().decode(privateKey);
                // 鏋勯�燩KCS8EncodedKeySpec瀵硅薄
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
                // KEY_ALGORITHM 鎸囧畾鐨勫姞瀵嗙畻娉�
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                // 鍙栫閽ュ寵瀵硅薄
                PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
                return priKey;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static String sign(byte[] data, PrivateKey priKey) {
            try {
                // 鐢ㄧ閽ュ淇℃伅鐢熸垚鏁板瓧绛惧悕
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
                // 瀵规暟鎹В瀵�
                Cipher cipher = Cipher.getInstance(rsaKeyFactory.getAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
