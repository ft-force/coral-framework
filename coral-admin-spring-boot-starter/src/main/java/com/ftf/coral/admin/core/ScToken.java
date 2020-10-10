package com.ftf.coral.admin.core;

import java.io.Serializable;
import java.util.UUID;

import com.ftf.coral.util.MD5Utils;
import com.ftf.coral.util.StringUtils;

public class ScToken implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PROTOCOL_GUEST = "90";
    public static final String PROTOCOL_LOGIN = "91";
    public static final String VERSION_01 = "01";
    public static final String CLIENT_SIGNATURE_KEY = "ftf";

    private String accountIdMD5;
    private String sessionToken;
    private String clientSignature;
    private boolean loginState = false;

    public String getAccountIdMD5() {
        return accountIdMD5;
    }

    public void setAccountIdMD5(String accountIdMD5) {
        this.accountIdMD5 = accountIdMD5;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getClientSignature() {
        return clientSignature;
    }

    public void setClientSignature(String clientSignature) {
        this.clientSignature = clientSignature;
    }

    public boolean isLogin() {
        return loginState;
    }

    public void setLoginState(boolean loginState) {
        this.loginState = loginState;
    }

    /**
     * 校验ip是否有效
     */
    public boolean isValidate(String clientIp) {

        String sign = "";

        if (loginState) {
            sign = MD5Utils.MD5Encode(
                StringUtils.join(PROTOCOL_LOGIN, VERSION_01, accountIdMD5, sessionToken, CLIENT_SIGNATURE_KEY));
        } else {
            sign = MD5Utils.MD5Encode(StringUtils.join(PROTOCOL_GUEST, VERSION_01, sessionToken, CLIENT_SIGNATURE_KEY));
        }

        return sign.equals(clientSignature);
    }

    public String accessToken(String clientIp) {

        if (loginState) {
            clientSignature = MD5Utils.MD5Encode(
                StringUtils.join(PROTOCOL_LOGIN, VERSION_01, accountIdMD5, sessionToken, CLIENT_SIGNATURE_KEY));

            return StringUtils.join(PROTOCOL_LOGIN, VERSION_01, accountIdMD5, sessionToken, clientSignature);

        } else {
            clientSignature =
                MD5Utils.MD5Encode(StringUtils.join(PROTOCOL_GUEST, VERSION_01, sessionToken, CLIENT_SIGNATURE_KEY));

            return StringUtils.join(PROTOCOL_GUEST, VERSION_01, sessionToken, clientSignature);
        }
    }

    public void upgrade(String accountId) {
        this.setLoginState(true);
        this.setAccountIdMD5(MD5Utils.MD5Encode(accountId));
    }

    /**
     * 解析token
     */
    public static ScToken parse(String accessToken) {

        if (accessToken.length() <= 4) {
            return null;
        }

        ScToken token = null;

        switch (accessToken.substring(0, 2)) {

            case PROTOCOL_GUEST:
                if (accessToken.length() != 68) {
                    return null;
                }
                token = new ScToken();
                token.setLoginState(false);
                token.sessionToken = accessToken.substring(4, 36);
                token.clientSignature = accessToken.substring(36);
                break;

            case PROTOCOL_LOGIN:
                if (accessToken.length() != 100) {
                    return null;
                }
                token = new ScToken();
                token.setLoginState(true);
                token.accountIdMD5 = accessToken.substring(4, 36);
                token.sessionToken = accessToken.substring(36, 68);
                token.clientSignature = accessToken.substring(68);
                break;

            default:
                // ignore
                break;
        }

        return token;
    }

    /**
     * 生成token
     */
    public static ScToken generateToken(String accountId) {

        ScToken token = new ScToken();
        token.setSessionToken(MD5Utils.MD5Encode(UUID.randomUUID().toString()));

        if (StringUtils.isNotBlank(accountId)) {
            token.setLoginState(true);
            token.setAccountIdMD5(MD5Utils.MD5Encode(accountId));
        } else {
            token.setLoginState(false);
        }

        return token;
    }
}
