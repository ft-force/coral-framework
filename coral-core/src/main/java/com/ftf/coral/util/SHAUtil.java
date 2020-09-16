package com.ftf.coral.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SHAUtil {
    private static final Logger logger = LoggerFactory.getLogger(SHAUtil.class);

    public static String encodeSHA256(String testString) {
        byte[] data = testString.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data);
            return bytes2Hex(digest);
        } catch (Exception e) {
            logger.warn("加密失败", e);
            return "";
        }
    }

    public static String shaEncode(String inStr) {

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");

            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] md5Bytes = sha.digest(byteArray);

            StringBuffer hexValue = new StringBuffer();

            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int)md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }

            return hexValue.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des.toUpperCase();
    }

    public static void main(String[] args) {
        String rqstSrcCode = "AMA_APP";
        String channelCode = "LOGIN_SERVICE";
        String rqstSn = "123456";
        String userName = "test";
        String checkSign = SHAUtil.encodeSHA256(rqstSrcCode + "_" + channelCode + ":" + rqstSn + "@" + userName);
        System.out.println(checkSign);
    }
}
