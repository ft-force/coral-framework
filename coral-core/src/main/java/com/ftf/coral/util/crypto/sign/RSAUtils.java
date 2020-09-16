package com.ftf.coral.util.crypto.sign;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSAUtils {

    // 签名算法
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    public static final String DEFAULT_ALGORITHM = "SHA256withRSA";

    /**
     * 生成公钥和私钥
     * 
     * @return RSA公钥和私钥
     * @throws NoSuchAlgorithmException
     *             找不到对应的算法
     */
    public static RSAKeyPeer generateRASKeyPeer() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAKeyPeer rasKey = new RSAKeyPeer();
        rasKey.setPrivateKey((RSAPrivateKey)keyPair.getPrivate());
        rasKey.setPublicKey((RSAPublicKey)keyPair.getPublic());
        return rasKey;
    }

    /**
     * 使用模和指数生成RSA公钥<br>
     * 注意：此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA /None/NoPadding
     * 
     * @param modulus
     *            模
     * @param exponent
     *            指数
     * @return RSA公钥
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey)keyFactory.generatePublic(keySpec);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用模和指数生成RSA私钥<br>
     * 注意：此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA /None/NoPadding
     * 
     * @param modulus
     *            模
     * @param exponent
     *            指数
     * @return RSA私钥
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static String sign(String content, RSAPrivateKey priKey) {
        try {
            return signString(content.getBytes("UTF-8"), priKey);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static String signString(byte[] contentBytes, RSAPrivateKey priKey) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(DEFAULT_ALGORITHM);
            signature.initSign(priKey);
            signature.update(contentBytes);
            byte[] signed = signature.sign();
            return Base64.getEncoder().encodeToString(signed);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] sign(byte[] contentBytes, RSAPrivateKey priKey) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(DEFAULT_ALGORITHM);
            signature.initSign(priKey);
            signature.update(contentBytes);
            return signature.sign();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validateSign(String content, String sign, RSAPublicKey pubKey) {
        try {
            return validateSign(content.getBytes("UTF-8"), sign, pubKey);
        } catch (Throwable e) {
            // ignore
            return false;
        }
    }

    public static boolean validateSign(byte[] contentBytes, String sign, RSAPublicKey pubKey) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(DEFAULT_ALGORITHM);

            signature.initVerify(pubKey);
            signature.update(contentBytes);

            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Throwable e) {
            // ignore
            return false;
        }
    }

    public static boolean validateSign(byte[] contentBytes, byte[] signBytes, RSAPublicKey pubKey) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(DEFAULT_ALGORITHM);

            signature.initVerify(pubKey);
            signature.update(contentBytes);

            return signature.verify(signBytes);
        } catch (Throwable e) {
            // ignore
            return false;
        }
    }

    public static String encryptToBase64(byte[] data, RSAPublicKey pubKey) {
        return Base64.getEncoder().encodeToString(encrypt(data, pubKey));
    }

    public static byte[] encrypt(byte[] data, RSAPublicKey pubKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptToBase64(byte[] data, RSAPrivateKey priKey) {
        return Base64.getEncoder().encodeToString(encrypt(data, priKey));
    }

    public static byte[] encrypt(byte[] data, RSAPrivateKey priKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, priKey);
            return cipher.doFinal(data);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptFromBase64(String src, RSAPrivateKey priKey) {
        return decrypt(Base64.getDecoder().decode(src), priKey);
    }

    public static byte[] decrypt(byte[] data, RSAPrivateKey priKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return cipher.doFinal(data);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptFromBase64(String src, RSAPublicKey pubKey) {
        return decrypt(Base64.getDecoder().decode(src), pubKey);
    }

    public static byte[] decrypt(byte[] data, RSAPublicKey pubKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}